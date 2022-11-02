package org.acme;

public class Log
{
    static void log(String msg)
    {
        System.out.printf(
                "[%d] %s%n"
                , ProcessHandle.current().pid()
                , msg
        );
    }
}
