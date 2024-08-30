import java.util.regex.Pattern;
import java.util.regex.Matcher;

class Solution {
    /**My solution
     * Runtime: 22ms Beats 14.11%
     * Memory: 45.64MB Beats 14.50%
    **/
    public boolean isPalindrome(String s) {
        String pattern = "[A-Za-z0-9]";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(s);
        StringBuilder sb = new StringBuilder();
        while(m.find()) {
            sb.append(m.group());
        }

        String result = sb.toString().toLowerCase();
        if (result.length() == 0 || result.length() == 1) {
            return true;
        };
        if (result.length() > 0 && result.length() % 2 == 0) {
            return false;
        }

        StringBuilder palinBuilder = new StringBuilder();
        for (int i = result.length()-1; i >= 0; i--) {
            palinBuilder.append(result.charAt(i));
        }
        String palin = palinBuilder.toString();
        System.out.println("palin: " + palin);
        if (!result.equals(palin)) {
            return false;
        }
        return true;
    }

    /** Book solution 1
     * Runtime: 2ms Beats 99.18%
     * Memory: 42.35MB Beats 99.24%
     **/
    public boolean isPalindrome2(String s) {
        int start = 0;
        int end = s.length()-1;
        while (start < end) {
            if (!Character.isLetterOrDigit(s.charAt(start))) {
                start++;
            }
            else if (!Character.isLetterOrDigit(s.charAt(end))) {
                end--;
            } else {
                if (Character.toLowerCase(s.charAt(start)) != Character.toLowerCase(s.charAt(end))) {
                    return false;
                }
                start++;
                end--;
            }
        }
        return true;
    }

    /** Book solution 2
     * Runtime: 16ms Beats 19.80%
     * Memory: 44.90MB Beats 31.04%
     **/
    public boolean isPalindrome3(String s) {
        String s_filtered = s.replaceAll("[^A-Za-z0-9]", "").toLowerCase();
        String s_filtered_reverse = new StringBuilder(s_filtered).reverse().toString();
        return s_filtered.equals(s_filtered_reverse);
    }
}