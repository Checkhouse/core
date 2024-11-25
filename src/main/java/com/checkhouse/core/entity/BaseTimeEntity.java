package com.checkhouse.core.entity;


import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseTimeEntity {
    @CreatedDate
    @Column(
            name = "created_at",
            updatable = false
    )
    private LocalDateTime createdDate; //생성시간

    @LastModifiedDate
    @Column(name = "updated_at")
    protected LocalDateTime lastModifiedDate; //수정시간

    @Column(
            name = "deleted_at",
            updatable = false
    )
    protected LocalDateTime deletedDate = null; //삭제 시간, Null일시 살아있음

    //soft delete 코드 추가
    public void setDeleted() {this.deletedDate = LocalDateTime.now();}
}
