import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
        String str = "KpymM7xbphWlD6AktfurcKMCXvrYpYPo01r5rfo9RLRa871SW3ZbPcEDoYZZUm8";
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        for (byte b : bytes) {
            System.out.print(b + " ");
        }
        byte[] bytes2 = new byte[64];
        for (int i = 0; i < 63; i++) {
            bytes2[i] = bytes[i];
        }
        bytes2[63] = 0x00;
        System.out.println();
        for (byte b : bytes2) {
            System.out.print(b + " ");
        }
        // byte数组转utf8字符串
        String res = new String(bytes2, StandardCharsets.UTF_8);

        System.out.println();
        System.out.println(str);
        System.out.println(res);
        char ch = 0x00;
        System.out.println(ch);
    }
}
