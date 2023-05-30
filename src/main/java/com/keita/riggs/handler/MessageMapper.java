package com.keita.riggs.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletOutputStream;

import java.io.IOException;

public class MessageMapper extends ObjectMapper {
    public MessageMapper(ServletOutputStream outputStream, Object object) throws IOException {
        super.writeValue(outputStream, object);
    }
}
