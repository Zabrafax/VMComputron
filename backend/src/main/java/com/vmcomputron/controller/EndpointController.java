package com.vmcomputron.controller;

import com.vmcomputron.cvmPackage.CvmRegisters;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api") //Путь для http запросов
public class EndpointController {
    //эндпоинты

    @GetMapping("/memory")
    public String[][] getMemory(
            @RequestParam("register") String registerParam) {

        if (registerParam == null || registerParam.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Параметр 'register' обязателен");
        }

        int baseAddress;
        try {
            baseAddress = switch (registerParam.toUpperCase()) {
                case "PC" -> CvmRegisters.getPC();
                case "SP" -> CvmRegisters.getSP();
                case "A"  -> CvmRegisters.getA();
                case "X"  -> CvmRegisters.getX();
                case "RH" -> CvmRegisters.getRH();
                case "RL" -> CvmRegisters.getRL();
                default -> Integer.parseInt(registerParam.trim(), 16);
            };
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Неверный регистр или адрес: " + registerParam);
        }

        int startAddress = (baseAddress & 0xFFFF) - 128;
        if (startAddress < 0) startAddress = 0;
        if (startAddress > 65536 - 256) startAddress = 65536 - 256;

        String[][] grid = new String[16][16];

        // Порядок: как у лампочек — слева направо, сверху вниз
        for (int row = 0; row < 16; row++) {
            for (int col = 0; col < 16; col++) {
                int address = startAddress + (row * 16 + col);
                int value = (address < 65536) ? (CvmRegisters.getM(address) & 0xFF) : 0;

                // РОВНО 2 СИМВОЛА, ВЕРХНИЙ РЕГИСТР, БЕЗ 0x
                grid[row][col] = String.format("%02X", value);
                // Примеры: "00", "FF", "A5", "0A", "1F" — всегда 2 символа!
            }
        }

        return grid;
    }
}