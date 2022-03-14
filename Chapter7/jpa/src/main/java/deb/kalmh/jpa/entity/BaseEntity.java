package deb.kalmh.jpa.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.Instant;

//모든 entity가 공통으로 가져야 할 속성
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract  class BaseEntity {
    @CreatedDate
    @Column(updatable = false)
    private Instant createAt; //언제 생성되었는가

    @LastModifiedDate
    @Column(updatable = true)
    private Instant updatedAt; //언제 마지막으로 수정되었는가

    public Instant getCreateAt() {
        return createAt;
    }
    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
