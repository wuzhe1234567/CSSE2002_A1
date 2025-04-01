package game.utility;

/**
 * A simple logger implementation that writes log messages to the standard output.
 */
public class ConsoleLogger implements Logger {
    @Override
    public void log(String message) {
        // 直接输出传入的消息，不做额外格式化
        System.out.println(message);
    }
}
