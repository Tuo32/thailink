package com.liu.thailink.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//interface return packaged class
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private String code;
    private String msg;
    private Object data;


    public static Result success(){
        return new Result("CODE_200","success",null);
    }

    public static Result success(Object data){
        return new Result("CODE_200","success",data);
    }

    public static Result error(String code, String msg){
        return new Result(code,msg,null);
    }
}
