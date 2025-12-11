package com.vmcomputron.controller;

import com.vmcomputron.cvmPackage.CvmRegisters;
import com.vmcomputron.model.Greeting;
import com.vmcomputron.model.Register;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController { // websocket
    //если надо чтото новое отправить надо как в дб создать файл поддержку на примере greeting
    // Клиент отправляет на /app/hello → сервер broadcast на /topic/greetings
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting handle(String message) {
        return new Greeting("Привет от сервера: " + message);
    }

    @MessageMapping("/registerUpdated")
    @SendTo("/topic/registers")
    public CvmRegisters handleRegisterUpdate(
            @Payload Integer value,
            @Header("register") String registerName) {

        CvmRegisters.updateRegister(registerName, value);
        return CvmRegisters.getCurrentState();
    }

    //    client.publish({
//        destination: '/app/registerUpdated',
//                body: JSON.stringify(42),                    // ← значение
//                headers: { 'register': 'A' }                 // ← имя регистра
//    });
//    client.subscribe('/topic/registers', (msg) => {
//    const regs = JSON.parse(msg.body);
//        setRegisters({
//                pc: regs.pc.toString(16).padStart(4, '0').toUpperCase(),
//                sp: regs.sp,
//                a: regs.a,
//                x: regs.x,
//                r: regs.r.toFixed(2),
//                rh: regs.rh,
//                rl: regs.rl
//    });
//    });

    @MessageMapping("/request/PC")
    @SendTo("/topic/register/PC")
    public Register handleRegisterUpdatePC() {
        return Register.pc(CvmRegisters.getPC());
    }

    // === SP ===
    @MessageMapping("/request/SP")
    @SendTo("/topic/register/SP")
    public Register handleRegisterUpdateSP() {
        return Register.sp(CvmRegisters.getSP());
    }

    // === A ===
    @MessageMapping("/request/A")
    @SendTo("/topic/register/A")
    public Register handleRegisterUpdateA() {
        return Register.a(CvmRegisters.getA());
    }

    // === X ===
    @MessageMapping("/request/X")
    @SendTo("/topic/register/X")
    public Register handleRegisterUpdateX() {
        return Register.x(CvmRegisters.getX());
    }

    // === RH ===
    @MessageMapping("/request/RH")
    @SendTo("/topic/register/RH")
    public Register handleRegisterUpdateRH() {
        return Register.rh(CvmRegisters.getRH());
    }

    // === RL ===
    @MessageMapping("/request/RL")
    @SendTo("/topic/register/RL")
    public Register handleRegisterUpdateRL() {
        return Register.rl(CvmRegisters.getRL());
    }

    // === R (float) — отдельно, потому что значение float ===
    @MessageMapping("/request/R")
    @SendTo("/topic/register/R")
    public Register handleRegisterUpdateR() {
        return Register.r(CvmRegisters.getR());
    }
//    client.publish({ destination: '/app/request/PC' });
//
//// Подписаться только на изменения PC
//    client.subscribe('/topic/register/PC', (msg) => {
//    const data = JSON.parse(msg.body);
//        setPc(data.newValue);
//        setCpuPanel(data.cpu);
//    });


}

