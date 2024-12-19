package org.example;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class ConsistentHashWithVirtualNodes {
    private final TreeMap<Long, String> circle = new TreeMap<>();
    private final int virtualNodeCount;  // 각 노드당 가상 노드 수

    public ConsistentHashWithVirtualNodes(int virtualNodeCount) {
        this.virtualNodeCount = virtualNodeCount;
    }

    public void addNode(String node) {
        // nodeName#0, nodeName#1, ... 형태로 가상 노드를 추가
        for (int i = 0; i < virtualNodeCount; i++) {
            String virtualNodeName = node + "#" + i;
            long hash = hash(virtualNodeName);
            circle.put(hash, node); // 가상 노드이지만 실제 노드명을 값으로 저장
        }
    }

    public void removeNode(String node) {
        // 해당 노드의 가상 노드를 모두 제거
        for (int i = 0; i < virtualNodeCount; i++) {
            String virtualNodeName = node + "#" + i;
            long hash = hash(virtualNodeName);
            circle.remove(hash);
        }
    }

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

    // 테스트 코드
    public static void main(String[] args) {
        // 가상 노드 수 3개
        ConsistentHashWithVirtualNodes ch = new ConsistentHashWithVirtualNodes(3);

        ch.addNode("NodeA");
        ch.addNode("NodeB");
        ch.addNode("NodeC");

        List<String> keys = Arrays.asList("user1", "user2", "myKey", "anotherKey", "foo", "bar");

        System.out.println("=== 초기 키 매핑 ===");
        for (String key : keys) {
            System.out.println(key + " -> " + ch.getNode(key));
        }

        System.out.println("\n=== NodeD 추가 후 ===");
        ch.addNode("NodeD");
        for (String key : keys) {
            System.out.println(key + " -> " + ch.getNode(key));
        }

        System.out.println("\n=== NodeB 제거 후 ===");
        ch.removeNode("NodeB");
        for (String key : keys) {
            System.out.println(key + " -> " + ch.getNode(key));
        }
    }
}

