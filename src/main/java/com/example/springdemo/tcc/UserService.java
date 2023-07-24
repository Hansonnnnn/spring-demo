package com.example.springdemo.tcc;

public interface UserService {
  @TccAction(name="userAccount", confirmMethod="confirm", cancelMethod="cancel")
  void tryDeduct();

  void confirm();

  void cancel();
}
