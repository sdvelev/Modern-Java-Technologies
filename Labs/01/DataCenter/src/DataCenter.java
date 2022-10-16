public class DataCenter {

    public static int getCommunicatingServersCount(int [][] map){

        if (map == null){
            return 0;
        }

        int m = map.length;
        int n = map[0].length;

        int[][] counted = new int[m][n];

        for (int i = 0; i < m; i++){

            int counter = 0;
            for (int j = 0; j < n; j++){
                if (map[i][j] == 1){
                    ++counter;
                }
            }

            if (counter > 1) {
                for(int j = 0; j < n; j++){
                    if (map[i][j] == 1){
                        counted[i][j] = 1;
                    }
                }
            }
        }

        for (int j = 0; j < n; j++){

            int counter = 0;
            for (int i = 0; i < m; i++){
                if (map[i][j] == 1){
                    ++counter;
                }
            }

            if (counter > 1) {
                for(int i = 0; i < m; i++){
                    if (map[i][j] == 1){
                        counted[i][j] = 1;
                    }
                }
            }
        }

        int result = 0;

        for(int i = 0; i < m; i++){
            for(int j = 0; j < n; j++){
                if (counted[i][j] == 1){
                    result += 1;
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(getCommunicatingServersCount(new int[][]{{1, 0}, {0, 1}}));
        System.out.println(getCommunicatingServersCount(new int[][]{{1, 0}, {1, 1}}));
        System.out.println(getCommunicatingServersCount(new int[][]{{1, 1, 0, 0}, {0, 0, 1, 0}, {0, 0, 1, 0}, {0, 0, 0, 1}}));
        System.out.println(getCommunicatingServersCount(new int [][]{{1,0,0,1},{0,0,1,1},{1,1,0,0}}));
        System.out.println(getCommunicatingServersCount(new int[][]{{1, 0}}));
        System.out.println(getCommunicatingServersCount(new int[][]{{1, 1}}));
        System.out.println(getCommunicatingServersCount(new int[][]{{}}));
    }
}
