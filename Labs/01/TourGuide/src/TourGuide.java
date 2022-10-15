public class TourGuide {


    public static int getBestSightseeingPairScore(int[] places){
        int len = places.length;

        int currentResult = 0;
        int maxResult = Integer.MIN_VALUE;

        for(int i = 0; i < len; i++) {

            for(int j = i + 1; j < len; j++){
               currentResult = places[i] + places[j] + i - j;

               if(currentResult > maxResult){
                   maxResult = currentResult;
               }

            }
        }

        return maxResult;
    }

    public static void main(String[] args) {
        System.out.println(getBestSightseeingPairScore(new int[]{8, 1, 5, 2,6}));
        System.out.println(getBestSightseeingPairScore(new int[]{1, 2}));
    }
}
