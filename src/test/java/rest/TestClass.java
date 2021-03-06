package rest;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static rest.DataGenerator.RegistrationInfo.setUpUser;

public class TestClass {

    @BeforeEach
    void headlessMode() {
        Configuration.headless = true;
    }

    private void loginForm(String login, String password) {

        open("http://localhost:9999");
        $("[data-test-id=login] input").setValue(login);
        $("[data-test-id=password] input").setValue(password);
        $("[data-test-id=action-login]").click();
    }

    @Test
    public void shouldLoginExistsActiveUser() {

        Form user = DataGenerator.RegistrationInfo.generateUserInfo("ru", "active");
        setUpUser(user);
        loginForm(user.getLogin(), user.getPassword());
        $(byText("Личный кабинет")).shouldBe(Condition.visible, Duration.ofSeconds(15));
    }

    @Test
    public void shouldNotLoginExistsBlockedUser() {

        Form user = DataGenerator.RegistrationInfo.generateUserInfo("ru", "blocked");
        setUpUser(user);
        loginForm(user.getLogin(), user.getPassword());

        $(withText("Пользователь заблокирован")).shouldBe(Condition.visible, Duration.ofSeconds(15));
    }

    @Test
    public void shouldNotLoginWithWrongUsername() {

        Form user = DataGenerator.RegistrationInfo.generateUserInfo("ru", "active");
        setUpUser(user);
        loginForm(DataGenerator.RegistrationInfo.makeUserName("ru"), user.getPassword());
        $(withText("Неверно указан логин или пароль")).shouldBe(Condition.visible, Duration.ofSeconds(15));
    }

    @Test
    public void shouldNotLoginWithWrongPassword() {

        Form user = DataGenerator.RegistrationInfo.generateUserInfo("ru", "active");
        setUpUser(user);
        loginForm(user.getLogin(), DataGenerator.RegistrationInfo.makeUserPassword("ru"));
        $(withText("Неверно указан логин или пароль")).shouldBe(Condition.visible, Duration.ofSeconds(15));
    }

}