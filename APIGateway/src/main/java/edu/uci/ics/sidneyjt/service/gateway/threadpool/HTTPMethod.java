package edu.uci.ics.sidneyjt.service.gateway.threadpool;

public enum HTTPMethod
{
    GET      ("GET"),
    HEAD     ("HEAD"),
    POST     ("POST"),
    PUT      ("PUT"),
    DELETE   ("DELETE"),
    CONNECT  ("CONNECT"),
    OPTIONS  ("OPTIONS"),
    TRACE    ("TRACE"),
    PATCH    ("PATCH");

    private final String type;

    HTTPMethod(String type)
    {
        this.type = type;
    }

    @Override
    public String toString()
    {
        return type;
    }
}
