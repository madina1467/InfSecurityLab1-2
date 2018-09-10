import java.util.Scanner;

public class MagicSquare {

    public void odd(int[][] magic) {

        int N = magic.length;
        int row = N - 1;
        int col = N / 2;
        magic[row][col] = 1;
        for (int i = 2; i <= N * N; i++) {
            if (magic[(row + 1) % N][(col + 1) % N] == 0) {
                row = (row + 1) % N;
                col = (col + 1) % N;
            } else {
                row = (row - 1 + N) % N;
            }
            magic[row][col] = i;
        }

    }///slslslls

    public void doublyEven(int[][] magicsqr) {
        int N = magicsqr.length;

        int miniSqrNum = N / 4; //size of boxes
        int cnt = 1;          //counter 1 to N*N
        int invCnt = N * N;     //counter N*N to 1
        for (int i = 0; i < N; i++) {

            for (int j = 0; j < N; j++) {

                if (j >= miniSqrNum && j < N - miniSqrNum) {
                    if (i >= miniSqrNum && i < N - miniSqrNum)
                        magicsqr[i][j] = cnt;    //central box
                    else
                        magicsqr[i][j] = invCnt; // up & down boxes

                } else if (i < miniSqrNum || i >= N - miniSqrNum) {
                    magicsqr[i][j] = cnt;             // 4 corners
                } else
                    magicsqr[i][j] = invCnt;    // left & right boxes

                cnt++;
                invCnt--;
            }

        }

    }

    public void singlyEven(int[][] magicsqr) {

        int N = magicsqr.length;
        int halfN = N / 2; //size of ABCD boxes
        int k = (N - 2) / 4; // to get 'noses' of A & D boxes
        int temp;

        int[] swapCol = new int[N]; // columns which need to swap between C-B & A-D
        int index = 0; // index of swapCol

        int[][] miniMagic = new int[halfN][halfN];
        odd(miniMagic);    //creating odd magic square for A box

        //creating 4 magic boxes
        for (int i = 0; i < halfN; i++)
            for (int j = 0; j < halfN; j++) {
                magicsqr[i][j] = miniMagic[i][j];              //A box
                magicsqr[i + halfN][j + halfN] = miniMagic[i][j] + halfN * halfN;   //B box
                magicsqr[i][j + halfN] = miniMagic[i][j] + 2 * halfN * halfN;       //C box
                magicsqr[i + halfN][j] = miniMagic[i][j] + 3 * halfN * halfN;       //D box
            }


        for (int i = 1; i <= k; i++)
            swapCol[index++] = i;

        for (int i = N - k + 2; i <= N; i++)
            swapCol[index++] = i;

        //swaping values between C-B & A-D by known columns
        for (int i = 1; i <= halfN; i++)
            for (int j = 1; j <= index; j++) {
                temp = magicsqr[i - 1][swapCol[j - 1] - 1];
                magicsqr[i - 1][swapCol[j - 1] - 1] = magicsqr[i + halfN - 1][swapCol[j - 1] - 1];
                magicsqr[i + halfN - 1][swapCol[j - 1] - 1] = temp;
            }

        //swaping noses
        temp = magicsqr[k][0];
        magicsqr[k][0] = magicsqr[k + halfN][0];
        magicsqr[k + halfN][0] = temp;


        temp = magicsqr[k + halfN][k];
        magicsqr[k + halfN][k] = magicsqr[k][k];
        magicsqr[k][k] = temp;
        //end of swaping

    }

    public void showMagicSqr(int[][] magicsqr) {
        int N = magicsqr.length;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++)
                System.out.print(magicsqr[i][j] + " ");

            System.out.println();
        }
    }

    public boolean isMagicSqr(int[][] magicsqr) {
        int N = magicsqr.length;
        int magicConst = (N * N * N + N) / 2;

        int rowsum = 0;
        int colsum = 0;
        int diag1 = 0;
        int diag2 = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                rowsum += magicsqr[i][j];
                colsum += magicsqr[j][i];
            }
            diag1 += magicsqr[i][i];
            diag2 += magicsqr[i][N - i - 1];
            if (rowsum != magicConst) {
                return false;
            }
            if (colsum != magicConst) {
                return false;
            }
            rowsum = 0;
            colsum = 0;
        }
        if (diag1 != magicConst || diag2 != magicConst)
            return false;

        return true;
    }

    public static int findNextPosition(int pos, int matrix[][]) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (pos == matrix[i][j])
                    return i * matrix.length + j;
            }
        }
        return 0;
    }

    public void encryption() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter matrix size: ");
        int N = scanner.nextInt();
        String encrypted = "";


        int[][] matrix = new int[N][N];


        if (N <= 2) throw new RuntimeException("A size of matrix must be bigger than 2");

        if (N % 2 == 1)
            odd(matrix);
        else if (N % 4 == 0)
            doublyEven(matrix);
        else
            singlyEven(matrix);

        showMagicSqr(matrix);

        System.out.println("Enter text to encrypt: ");

        String text = null;
        if (scanner.hasNext())
            text = scanner.next();
        int remainder = 0;
        int count = 0;

        if (text != null) {

            char output[][] = new char[N][N];
            int position = 1;
            int charPosition = 0;
            int l = 0;

            count = (int) Math.ceil((text.length() * 1.0) / (N * N * 1.0));
            remainder = text.length() % (N * N);

            for (int k = 0; k < text.length(); k++) {
                char character = text.charAt(k);
                if (position == (N * N)+1) {
                    position = 1;
                    count--;
//                    if(count == 1)
//                        for (int i = 0; i < N; i++)
//                            for (int j = 0; j < N; j++)
//                                output[i][j] = '_';

                }


                charPosition = findNextPosition(position, matrix);
                output[charPosition / N][charPosition % N] = character;

                if (position == (N * N)){
                    for (int i = 0; i < N; i++) {
                        for (int j = 0; j < N; j++) {
                                encrypted += output[i][j];
                                output[i][j] = '_';
                        }
                    }
                }
                if(count == 1 && remainder != 0 && k == text.length()-1){
                    for (int i = 0; i < N; i++) {
                        for (int j = 0; j < N; j++) {
                            encrypted += output[i][j];

                        }
                    }
                }
                position++;

            }
        }



        char[] charArrayOfEncrypted = encrypted.toCharArray();
        int lenthOfEncryptedText = charArrayOfEncrypted.length;
        count = (int) Math.ceil((lenthOfEncryptedText * 1.0) / (N * N * 1.0));


        int ccount = 0;
        while(count!=ccount){
            System.out.println("Cycle #" + (ccount+1));
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    System.out.print(matrix[i][j]) ;
                    if(matrix[i][j] < 10)
                        System.out.print(" ");
                System.out.print("-" + charArrayOfEncrypted[(i * N) + (j) + (ccount * N * N)] + "  |  ");
                }
                System.out.println();
            }
            ccount++;
        }

        System.out.println("Text to encrypt: " + text);
        System.out.println("Encrypted text : " + encrypted);

    }

    public void decryption() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter matrix count: ");
        int N = scanner.nextInt();
        int[][] matrix = new int[N][N];
        System.out.println("Enter the matrix: ");
        int k = 1;
        String toDecrypt = "";
        char output[][] = new char[N][N];
        if (scanner.hasNextInt() && k != N * N + 1) {
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    matrix[i][j] = scanner.nextInt();
                    k++;
                }
            }

        }

        if (isMagicSqr(matrix)) {
            System.out.println("Enter the word:");
            //String toDecrypt = "";
            if (scanner.hasNext()) {
                toDecrypt += scanner.next();
            }
            String test = "";
            if (toDecrypt != null) {

                int position = 1;
                int charPosition = 0;
                String decrypted = "";
                int count = (int) Math.ceil(toDecrypt.length() * 1.0 / (N * N * 1.0));// + (toDecrypt.length() % (N * N));
                System.out.println("Count: " + count);
                int ccount = 0;
                char a = '-';

                    for (int m = 0; m < toDecrypt.length(); m++) {
                        if (position == (N * N) + 1) {
                            position = 1;
                            ccount++;
                        }
                        charPosition = findNextPosition(position, matrix);
                        if((charPosition) + (ccount * N * N) < toDecrypt.length()-1)
                            a = toDecrypt.charAt((charPosition) + (ccount * N * N));
                        decrypted += a;
                        position++;


                }
                System.out.println("Text to Decrypt: " + toDecrypt);
                System.out.println("Decrypted Text:  " + decrypted);


                //showMagicSqr(matrix);
            }


        }else{
            System.out.println("Try again! It's not magic square");
        }
        //4 9 2 3 5 7 8 1 6
        //rowetuiqyd_as___p_
    }




    public static void main(String[] args) {
        MagicSquare ms = new MagicSquare();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Menu: \n 1 - Encrypt\n 2 - Decrypt");
            System.out.print("Choose option: ");

            switch (scanner.nextInt()) {
                case 1:
                    ms.encryption();
                case 2:
                    ms.decryption();
                default:
                    System.out.println("Invalid option!");

            }
        }

    }
}
