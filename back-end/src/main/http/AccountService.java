package main.http;

import com.google.inject.name.Named;
import com.google.sitebricks.At;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Request;
import com.google.sitebricks.headless.Service;
import com.google.sitebricks.http.Delete;
import com.google.sitebricks.http.Get;
import com.google.sitebricks.http.Post;
import com.google.sitebricks.http.Put;
import com.sun.imageio.plugins.png.PNGImageReader;
import main.core.User;
import main.persistent.AccountRepository;
import main.persistent.DuplicationException;
import main.transport.Json;
import main.util.DateUtil;
import main.util.Encoder;

import javax.imageio.*;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.inject.Inject;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author Stanislav Valov <stanislav.valov@clouway.com>
 */
@Service
@At("/api/v1/accounts")
class AccountService {

  private AccountRepository repository;

  @Inject
  public AccountService(AccountRepository repository) {
    this.repository = repository;
  }

  @Get
  @At("/findAll")
  Reply<?> findAll() {
    List<User> users = repository.findAll();
    List<UserDTO> dtos = newArrayList();

    if (!users.isEmpty()) {
      dtos = adapt(users);
    }

    return Reply.with(dtos).as(Json.class).ok();
  }

  @Post
  Reply<?> create(Request request) {
    UserDTO dto = request.read(UserDTO.class).as(Json.class);

    User user = adapt(dto);

    try {
      repository.create(user);
    } catch (DuplicationException e) {
        return Reply.with(e.getMessage()).badRequest();
    }

    return Reply.with("User with nickname: "+user.nickname + " was created!").ok();
  }

  @Put
  public Reply<?> update(Request request) {
    UserDTO dto = request.read(UserDTO.class).as(Json.class);

    User user = adapt(dto);

    repository.update(user);

    return Reply.with("User with nickname: "+user.nickname + " was updated!").ok();
  }

  @Delete
  @At("/delete/:nickname")
  public Reply<?> delete(@Named("nickname") String nickname) {
    repository.delete(nickname);

    return Reply.with("User with nickname: "+nickname + " was removed!").ok();
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

  private List<UserDTO> adapt(List<User> users) {
    List<UserDTO> dtos = newArrayList();

    for (User user : users) {
      dtos.add(adapt(user));
    }

    return dtos;
  }

  private UserDTO adapt(User user) {
    String dateOfBirth = DateUtil.formatToString(user.dateOfBirth);

    return new UserDTO(user.nickname, user.firstName, user.lastName, user.email,
        dateOfBirth, user.imageData);
  }

  private User adapt(UserDTO dto) {
    Date dateOfBirth = DateUtil.formatToDate(dto.dateOfBirth);
//    String image = Encoder.convertToString(dto.imageData);

    return new User(dto.nickname, dto.firstName, dto.lastName, dto.email,
        dateOfBirth, dto.imageData);
  }
}
