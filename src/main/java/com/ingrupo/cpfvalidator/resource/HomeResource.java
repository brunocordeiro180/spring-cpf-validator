package com.ingrupo.cpfvalidator.resource;

import com.ingrupo.cpfvalidator.dto.IdentificadorDto;
import com.ingrupo.cpfvalidator.models.User;
import com.ingrupo.cpfvalidator.models.Requisition;
import com.ingrupo.cpfvalidator.repository.UserCostRepository;
import com.ingrupo.cpfvalidator.repository.UserRepository;
import com.ingrupo.cpfvalidator.util.ValidarIdentificador;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class HomeResource {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserCostRepository userCostRepository;

    @PostMapping("/validate")
    public String validate(@RequestBody IdentificadorDto identificadorDto){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByUsername(auth.getPrincipal().toString());

        if(user.isPresent()){

            ValidarIdentificador validarIdentificador = new ValidarIdentificador();
            String returnMessage;
            Boolean isValid = false;

            switch (identificadorDto.getTipo()){
                case "CPF":
                    isValid = validarIdentificador.isCPF(identificadorDto.getValue());
                    returnMessage = isValid ? "CPF Válido" : "CPF Inválido";
                    break;
                case "CNPJ":
                    isValid = validarIdentificador.isCNPJ(identificadorDto.getValue());
                    returnMessage =  isValid ? "CNPJ Válido" : "CNPJ Inválido";
                break;
                default:
                    returnMessage = "Tipo Inválido";
            }

            //Salva requisicao no historico

            User userObj = user.get();

            Requisition requisition = new Requisition();
            requisition.setUser(userObj);

            userCostRepository.save(requisition);

            return returnMessage;

        }else{
            return "Usuário não logado no sistema";
        }
    }

    @GetMapping("/requisitions")
    public ResponseEntity<String> getRequisitions(){

        JSONObject jsonObject = new JSONObject();
        List<Requisition> requisitions = userCostRepository.findAll();
        Map<User, List<Requisition>> groupedByUser = requisitions.stream()
                .collect(Collectors.groupingBy(Requisition::getUser));

        groupedByUser.forEach((user, userCosts) -> {
            Map<Integer, List<Requisition>> groupByDate = userCosts.stream()
                    .collect(Collectors.groupingBy(c -> {
                        Date date = c.getDate();
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        return cal.get(Calendar.MONTH);
                    }));

            JSONObject jsonObject1 = new JSONObject();
            groupByDate.forEach((month, values) -> {
                BigDecimal totalCost = BigDecimal.valueOf(0.10).multiply(BigDecimal.valueOf(values.size()));
                try {
                    jsonObject1.put(String.valueOf(month), totalCost.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });

            try {
                jsonObject.put(user.getUsername(), jsonObject1);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        });

        return ResponseEntity.ok(jsonObject.toString());
    }
}
