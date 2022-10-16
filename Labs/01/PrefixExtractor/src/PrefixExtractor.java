import java.util.Arrays;

public class PrefixExtractor {
    public static String getLongestCommonPrefix(String[] words) {

        if(words == null || words.length == 0){
            return "";
        }

        Arrays.sort(words);

        String first = words[0];
        String last = words[words.length-1];

        StringBuilder result = new StringBuilder();

        for(int i = 0; i < first.length(); i++){

            if(first.charAt(i) == last.charAt(i)) {
                result.append(first.charAt(i));
                continue;
            }

            break;
        }
        return result.toString();
    }

    public static void main(String[] args) {
        System.out.println(getLongestCommonPrefix(new String[]{"flower", "flow", "flight"}));
        System.out.println(getLongestCommonPrefix(new String[]{"dog", "racecar", "car"}));
        System.out.println(getLongestCommonPrefix(new String[]{"cat"}));
        System.out.println(getLongestCommonPrefix(new String[]{}));
        System.out.println(getLongestCommonPrefix(null));
    }
}
