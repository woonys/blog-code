package org.example;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class HashComparisonExample {
    public static void main(String[] args) {
        // 테스트용 키들
        List<String> keys = Arrays.asList("user1", "user2", "user3", "user4", "user5",
                                          "myKey", "anotherKey", "foo", "bar", "test",
                                          "hello", "world", "key123", "abc", "xyz");

        // 전통 해싱 클러스터 생성 (초기 노드: 3대)
        TraditionalHashingCluster traditionalCluster = new TraditionalHashingCluster();
        traditionalCluster.addNode("NodeA");
        traditionalCluster.addNode("NodeB");
        traditionalCluster.addNode("NodeC");

        // 안정 해싱 클러스터 생성 (초기 노드: 3대)
        ConsistentHashingCluster consistentCluster = new ConsistentHashingCluster();
        consistentCluster.addNode("NodeA");
        consistentCluster.addNode("NodeB");
        consistentCluster.addNode("NodeC");

        // 초기 매핑
        System.out.println("==== 초기 매핑 결과 ====");
        Map<String, String> initialTraditionalMapping = mapKeys(traditionalCluster, keys);
        Map<String, String> initialConsistentMapping = mapKeys(consistentCluster, keys);

        System.out.println("전통 해싱: " + initialTraditionalMapping);
        System.out.println("안정 해싱: " + initialConsistentMapping);

        // 노드 추가
        System.out.println("\n==== 노드 추가 (NodeD) 후 ====");
        traditionalCluster.addNode("NodeD");
        consistentCluster.addNode("NodeD");

        Map<String, String> afterAddTraditionalMapping = mapKeys(traditionalCluster, keys);
        Map<String, String> afterAddConsistentMapping = mapKeys(consistentCluster, keys);

        System.out.println("전통 해싱: " + afterAddTraditionalMapping);
        System.out.println("안정 해싱: " + afterAddConsistentMapping);


        int traditionalReassignedOnAdd = countReassignedKeys(initialTraditionalMapping, afterAddTraditionalMapping);
        int consistentReassignedOnAdd = countReassignedKeys(initialConsistentMapping, afterAddConsistentMapping);

        System.out.println("전통 해싱 추가 후 재할당된 키 수: " + traditionalReassignedOnAdd);
        System.out.println("안정 해싱 추가 후 재할당된 키 수: " + consistentReassignedOnAdd);

        // 노드 제거 (NodeB 제거)
        System.out.println("\n==== 노드 제거 (NodeB) 후 ====");
        traditionalCluster.removeNode("NodeB");
        consistentCluster.removeNode("NodeB");

        Map<String, String> afterRemoveTraditionalMapping = mapKeys(traditionalCluster, keys);
        Map<String, String> afterRemoveConsistentMapping = mapKeys(consistentCluster, keys);

        System.out.println("전통 해싱: " + afterRemoveTraditionalMapping);
        System.out.println("안정 해싱: " + afterRemoveConsistentMapping);

        int traditionalReassignedOnRemove = countReassignedKeys(afterAddTraditionalMapping, afterRemoveTraditionalMapping);
        int consistentReassignedOnRemove = countReassignedKeys(afterAddConsistentMapping, afterRemoveConsistentMapping);

        System.out.println("전통 해싱 제거 후 재할당된 키 수: " + traditionalReassignedOnRemove);
        System.out.println("안정 해싱 제거 후 재할당된 키 수: " + consistentReassignedOnRemove);

        // 결과 요약
        System.out.println("\n==== 결과 요약 ====");
        System.out.println("노드 추가 시 전통 해싱 vs 안정 해싱 재할당 키 수: "
                           + traditionalReassignedOnAdd + " vs " + consistentReassignedOnAdd);
        System.out.println("노드 제거 시 전통 해싱 vs 안정 해싱 재할당 키 수: "
                           + traditionalReassignedOnRemove + " vs " + consistentReassignedOnRemove);
    }

    private static Map<String, String> mapKeys(Cluster cluster, List<String> keys) {
        Map<String, String> mapping = new LinkedHashMap<>();
        for (String key : keys) {
            String node = cluster.getNode(key);
            mapping.put(key, node);
        }
        return mapping;
    }

    private static int countReassignedKeys(Map<String, String> beforeMap, Map<String, String> afterMap) {
        int count = 0;
        for (Map.Entry<String, String> entry : beforeMap.entrySet()) {
            String key = entry.getKey();
            String beforeNode = entry.getValue();
            String afterNode = afterMap.get(key);
            if (!beforeNode.equals(afterNode)) {
                count++;
            }
        }
        return count;
    }

    // 공통 인터페이스
    interface Cluster {
        void addNode(String node);
        void removeNode(String node);
        String getNode(String key);
    }

    // 전통 해싱 구현 (nodeIndex = hash(key) % nodeCount)
    static class TraditionalHashingCluster implements Cluster {
        private List<String> nodes = new ArrayList<>();

        @Override
        public void addNode(String node) {
            nodes.add(node);
        }

        @Override
        public void removeNode(String node) {
            nodes.remove(node);
        }

        @Override
        public String getNode(String key) {
            if (nodes.isEmpty()) return null;
            int hash = Math.abs(key.hashCode());
            int index = hash % nodes.size();
            return nodes.get(index);
        }
    }

    // Consistent Hashing 구현
    static class ConsistentHashingCluster implements Cluster {
        private final TreeMap<Long, String> circle = new TreeMap<>();

        @Override
        public void addNode(String node) {
            long hash = hash(node);
            circle.put(hash, node);
        }

        @Override
        public void removeNode(String node) {
            long hash = hash(node);
            circle.remove(hash);
        }

        @Override
        public String getNode(String key) {
            if (circle.isEmpty()) {
                return null;
            }
            long h = hash(key);
            Map.Entry<Long, String> entry = circle.ceilingEntry(h);
            if (entry == null) {
                return circle.firstEntry().getValue();
            }
            return entry.getValue();
        }

        private long hash(String key) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(key.getBytes());
                byte[] digest = md.digest();
                long h = 0;
                for (int i = 0; i < 8 && i < digest.length; i++) {
                    h <<= 8;
                    h |= ((int) digest[i]) & 0xFF;
                }
                return h & 0x7FFFFFFFFFFFFFFFL;
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("MD5 algorithm not found", e);
            }
        }
    }
}