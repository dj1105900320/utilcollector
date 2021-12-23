package utils.collector.pumpX.protocolcore;

import cn.hutool.core.util.HexUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Netty ByteBuf工具类
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public class ByteBufUtils {

    public static void main(String[] args) {
        ByteBuf buf = Unpooled.buffer();
        buf.writeShort(5);
        buf.writeByte(3);
        buf.writeInt(4);
        int num = readInt(buf, 2);
        int num2 = readInt(buf,1);
        int num3 = readInt(buf,4);
        System.out.println(num);
        System.out.println(num2);
        System.out.println(num3);
    }

    // 读取ByteBuf中指定字节长度的数字
    public static int readInt(ByteBuf input, int length) {
        int value;
        switch (length) {
            case 1:
                value = input.readUnsignedByte();
                break;
            case 2:
                value = input.readUnsignedShort();
                break;
            case 3:
                value = input.readUnsignedMedium();
                break;
            case 4:
                value = input.readInt();
                break;
            default:
                throw new RuntimeException("unsupported length: " + length + " (expected: 1, 2, 3, 4)");
        }
        return value;
    }

    // 向ByteBuf中写入指定长度的int值
    public static void writeInt(ByteBuf output, int length, int value) {
        switch (length) {
            case 1:
                output.writeByte(value);
                break;
            case 2:
                output.writeShort(value);
                break;
            case 3:
                output.writeMedium(value);
                break;
            case 4:
                output.writeInt(value);
                break;
            default:
                throw new RuntimeException("unsupported length: " + length + " (expected: 1, 2, 3, 4)");
        }
    }

    // 从指定位置获取指定长度的int值
    public static int getInt(ByteBuf input, int index, int length) {
        int value;
        switch (length) {
            case 1:
                value = input.getUnsignedByte(index);
                break;
            case 2:
                value = input.getUnsignedShort(index);
                break;
            case 3:
                value = input.getUnsignedMedium(index);
                break;
            case 4:
                value = input.getInt(index);
                break;
            default:
                throw new RuntimeException("unsupported length: " + length + " (expected: 1, 2, 3, 4)");
        }
        return value;
    }

    public static void setInt(ByteBuf output, int index, int length, int value) {
        switch (length) {
            case 1:
                output.setByte(index, value);
                break;
            case 2:
                output.setShort(index, value);
                break;
            case 3:
                output.setMedium(index, value);
                break;
            case 4:
                output.setInt(index, value);
                break;
            default:
                throw new RuntimeException("unsupported length: " + length + " (expected: 1, 2, 3, 4)");
        }
    }

    public static void writeFixedLength(ByteBuf output, int length, byte[] bytes) {
        int srcPos = length - bytes.length;
        if (srcPos > 0) {
            output.writeBytes(bytes);
            output.writeBytes(new byte[srcPos]);
        } else if (srcPos < 0) {
            output.writeBytes(bytes, -srcPos, length);
        } else {
            output.writeBytes(bytes);
        }
    }

    public static boolean startsWith(ByteBuf haystack, byte[] prefix) {
        for (int i = 0, j = haystack.readerIndex(); i < prefix.length; ) {
            if (prefix[i++] != haystack.getByte(j++)) {
                return false;
            }
        }
        return true;
    }
}