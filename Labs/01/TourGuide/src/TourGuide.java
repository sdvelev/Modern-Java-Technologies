public class TourGuide {

    public static int getBestSightseeingPairScore(int[] places){

        if (places == null) {
            return 0;
        }

        int len = places.length;

        if (len == 0) {
            return 0;
        }

        if (len == 1) {
            return places[0];
        }

        int currentResult;
        int maxResult = Integer.MIN_VALUE;

        for (int i = 0; i < len; i++) {

            for (int j = i + 1; j < len; j++){
               currentResult = places[i] + places[j] + i - j;

               if(currentResult > maxResult){
                   maxResult = currentResult;
               }

            }
        }

        return maxResult;
    }

    public static void main(String[] args) {
        System.out.println(getBestSightseeingPairScore(new int[]{8, 1, 5, 2, 6}));
        System.out.println(getBestSightseeingPairScore(new int[]{2, 5, 6, 1, 8}));
        System.out.println(getBestSightseeingPairScore(new int[]{1, 2}));
        System.out.println(getBestSightseeingPairScore(new int[]{6}));
        System.out.println(getBestSightseeingPairScore(null));
        System.out.println(getBestSightseeingPairScore(new int[]{}));
        System.out.println(getBestSightseeingPairScore(new int[]{6, 21}));
    }
}
