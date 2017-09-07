package http;

import com.google.inject.name.Named;
import com.google.sitebricks.At;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Request;
import com.google.sitebricks.headless.Service;
import com.google.sitebricks.http.Delete;
import com.google.sitebricks.http.Get;
import com.google.sitebricks.http.Post;
import com.google.sitebricks.http.Put;
import core.Account;
import http.security.UserDTO;
import persistent.AccountRepository;
import persistent.DuplicationException;
import transport.Json;
import util.DateUtil;
import util.Encoder;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author Stanislav Valov <stanislav.valov@clouway.com>
 */
@Service
@At("/api/v1/accounts")
public class AccountService {

  private AccountRepository repository;

  @Inject
  public AccountService(AccountRepository repository) {
    this.repository = repository;
  }

  @Get
  @At("/findAll")
  Reply<?> findAll() {
    List<Account> accounts = repository.findAll();
    List<AccountDTO> dtos = newArrayList();

    if (!accounts.isEmpty()) {
      dtos = adapt(accounts);
    }

    return Reply.with(dtos).as(Json.class).ok();
  }

  @Post
  Reply<?> create(Request request) {
    AccountDTO dto = request.read(AccountDTO.class).as(Json.class);

    Account account = adapt(dto);

    try {
      repository.create(account);
    } catch (DuplicationException e) {
        return Reply.with(e.getMessage()).badRequest();
    }

    return Reply.with("Account with nickname: "+ account.nickname + " was created!").ok();
  }

  @Put
  public Reply<?> update(Request request) {
    AccountDTO dto = request.read(AccountDTO.class).as(Json.class);

    Account account = adapt(dto);

    repository.update(account);

    return Reply.with("Account with nickname: "+ account.nickname + " was updated!").ok();
  }

  @Delete
  @At("/delete/:nickname")
  public Reply<?> delete(@Named("nickname") String nickname) {
    repository.delete(nickname);

    return Reply.with("Account with nickname: "+nickname + " was removed!").ok();
  }

  @Post
  @At("/:nickname/image/")
  public Reply<?> importImage(@Named("nickname") String nickname, Request request){
    ByteArrayOutputStream stream = new ByteArrayOutputStream();

    String imageData;

    try {
      request.readTo(stream);
      imageData = Encoder.convertToString(stream.toByteArray());
      stream.close();

    } catch (IOException e) {
      return Reply.with("Image addition failed for user with nickname: " + nickname).badRequest();
    }

    repository.importImage(nickname, imageData);

    return Reply.with("Image was added for user with nickname: " + nickname).ok();
  }

  private List<AccountDTO> adapt(List<Account> accounts) {
    List<AccountDTO> dtos = newArrayList();

    for (Account account : accounts) {
      dtos.add(adapt(account));
    }

    return dtos;
  }

  private AccountDTO adapt(Account account) {
    String dateOfBirth = DateUtil.formatToString(account.dateOfBirth);

    return new AccountDTO(account.nickname, account.firstName, account.lastName, account.email,
        dateOfBirth, account.imageData);
  }

  private Account adapt(AccountDTO dto) {
    Date dateOfBirth = DateUtil.formatToDate(dto.dateOfBirth);

    return new Account(dto.nickname, dto.firstName, dto.lastName, dto.email,
        dateOfBirth, dto.imageData);
  }
}
