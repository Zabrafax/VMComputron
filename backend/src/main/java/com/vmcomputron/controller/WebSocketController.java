package com.vmcomputron.controller;

import com.vmcomputron.cvmPackage.CvmRegisters;
import com.vmcomputron.model.Greeting;
import com.vmcomputron.model.Register;
import com.vmcomputron.model.RegisterUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController { // websocket

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }


    //если надо чтото новое отправить надо как в дб создать файл поддержку на примере greeting
    // Клиент отправляет на /app/hello → сервер broadcast на /topic/greetings
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting handle(String message) {
        return new Greeting("Привет от сервера: " + message);
    }


    @MessageMapping("/registerUpdated")
    @SendTo("/topic/registers")
    public void handleRegisterUpdate(@Payload RegisterUpdateRequest request) {

        CvmRegisters.updateRegister(request.register(), request.newValue());

        switch (request.register()) {
            case "PC":
                messagingTemplate.convertAndSend("/topic/register/PC", Register.pc(CvmRegisters.getPC()));
                break;
            case "SP":
                messagingTemplate.convertAndSend("/topic/register/SP", Register.sp(CvmRegisters.getSP()));
                break;
            case "A":
                messagingTemplate.convertAndSend("/topic/register/A", Register.a(CvmRegisters.getA()));
                break;
            case "X":
                messagingTemplate.convertAndSend("/topic/register/X", Register.x(CvmRegisters.getX()));
                break;
            case "RH":
                messagingTemplate.convertAndSend("/topic/register/RH", Register.rh(CvmRegisters.getRH()));
                break;
            case "RL":
                messagingTemplate.convertAndSend("/topic/register/RL", Register.rl(CvmRegisters.getRL()));
                break;
            case "R":
                messagingTemplate.convertAndSend("/topic/register/R", Register.r(CvmRegisters.getR()));
                break;
            default:
                throw new IllegalArgumentException("Unknown register: " + request.register());
        }
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


//    @Scheduled(fixedRate = 2000)  // можно 100, 200, 1000 — как хочешь
//    public void broadcastRegistersPeriodically() {
//        // Отправляем каждый регистр в свой топик
//        messagingTemplate.convertAndSend("/topic/register/PC",  Register.pc(CvmRegisters.getPC()));
//        messagingTemplate.convertAndSend("/topic/register/SP",  Register.sp(CvmRegisters.getSP()));
//        messagingTemplate.convertAndSend("/topic/register/A",   Register.a(CvmRegisters.getA()));
//        messagingTemplate.convertAndSend("/topic/register/X",   Register.x(CvmRegisters.getX()));
//        messagingTemplate.convertAndSend("/topic/register/RH",  Register.rh(CvmRegisters.getRH()));
//        messagingTemplate.convertAndSend("/topic/register/RL",  Register.rl(CvmRegisters.getRL()));
//        messagingTemplate.convertAndSend("/topic/register/R",   Register.r(CvmRegisters.getR()));
//    }
//    useEffect(() => {
//        if (!client.current) return;
//
//    const subscriptions = [
//        '/topic/register/PC',
//                '/topic/register/SP',
//                '/topic/register/A',
//                '/topic/register/X',
//                '/topic/register/RH',
//                '/topic/register/RL',
//                '/topic/register/R'
//    ];
//
//        subscriptions.forEach(topic => {
//                client.current.subscribe(topic, (message) => {
//            const data = JSON.parse(message.body);
//        setRegisters(prev => ({
//                ...prev,
//                [data.register.toLowerCase()]: data.newValue
//            }));
//        setCpuPanel(data.cpu);  // ← лампочки обновляются автоматически!
//        });
//    });
//
//        return () => {
//                subscriptions.forEach(topic => client.current?.unsubscribe(topic));
//    };
//    }, []);

}

