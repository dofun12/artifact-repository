package org.lemanoman.artifactrepository.dto;

import java.util.List;

public class ResponseDTO {
    private boolean success = false;
    private boolean hasData = false;
    private String detail;
    private Object data;

    public ResponseDTO() {
    }

    public static ResponseDTO fail(String message){
        return new ResponseDTO().failed(message);
    }

    public static ResponseDTO ok(List list){
        return new ResponseDTO(list).success();
    }

    public static ResponseDTO ok(){
        return new ResponseDTO().success();
    }

    public static ResponseDTO ok(Object object){
        return new ResponseDTO(object).success();
    }

    public ResponseDTO(List list) {
        if (list != null && !list.isEmpty()) {
            this.hasData = true;
        }
        this.data = list;
    }

    public ResponseDTO(Object object, boolean isList) {
        if (isList) {
            List list = (List) object;
            this.data = list;
            this.hasData = true;
            this.success = true;
        } else {
            this.data = object;
            this.hasData = true;
        }
    }

    public ResponseDTO(Object model) {
        if (model != null) {
            this.hasData = true;
            this.success = true;
        }
        this.data = model;
    }

    public ResponseDTO failed(String code, String path) {
        this.success = false;
        this.detail = code + " - " + path;
        return this;
    }

    public ResponseDTO failed(Exception ex) {
        this.success = false;
        if (ex != null) {
            this.detail = ex.getMessage();
        } else {
            this.detail = "Erro bizarro...";
        }

        return this;
    }

    public ResponseDTO failed(String error) {
        this.success = false;
        this.detail = error;
        return this;
    }

    public ResponseDTO success() {
        this.success = true;
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isHasData() {
        return hasData;
    }

    public void setHasData(boolean hasData) {
        this.hasData = hasData;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

}
