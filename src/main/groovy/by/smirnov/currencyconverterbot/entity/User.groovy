package by.smirnov.currencyconverterbot.entity

import groovy.transform.Canonical
import groovy.transform.EqualsAndHashCode
import groovy.transform.builder.Builder

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import java.sql.Timestamp

@Canonical
@EqualsAndHashCode(excludes = ['registeredAt'])
@Builder
@Entity
@Table(name = "users")
class User {

    @Id
    @Column(name="chat_id")
    Long chatId
    @Column(name="first_name")
    String firstName
    @Column(name="last_name")
    String lastName
    @Column(name="user_name")
    String userName
    @Column(name="registered_at")
    Timestamp registeredAt
}
