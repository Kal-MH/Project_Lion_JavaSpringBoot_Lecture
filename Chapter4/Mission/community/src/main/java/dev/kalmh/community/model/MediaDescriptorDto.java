package dev.kalmh.community.model;

public class MediaDescriptorDto {
    private Integer status; //요청 처리 결과
    private String message; //상태 설명
    private String originalName; //사용자가 보내준 파일 이름
    private String resourcePath; //url

    public MediaDescriptorDto() {
    }

    public MediaDescriptorDto(Integer status, String message, String originalName, String resourcePath) {
        this.status = status;
        this.message = message;
        this.originalName = originalName;
        this.resourcePath = resourcePath;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    @Override
    public String toString() {
        return "MediaDescriptorDto{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", originalName='" + originalName + '\'' +
                ", resourcePath='" + resourcePath + '\'' +
                '}';
    }
}
