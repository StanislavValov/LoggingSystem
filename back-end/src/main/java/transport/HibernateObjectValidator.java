package transport;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import core.Error;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

/**
 * Validates DTO objects coming from the client side.
 *
 * @author Stanislav Valov <Stanislav.Valov@experian.com>
 */
public class HibernateObjectValidator implements ObjectValidator {
  private final Validator validator;

  @Inject
  public HibernateObjectValidator(Validator validator){
    this.validator = validator;
  }

  @Override
  public List<Error> validate(Object dto) {
    Set<ConstraintViolation<Object>> violations = validator.validate(dto);
    List<Error> result = Lists.newArrayList();

    for (ConstraintViolation<Object> each : violations) {
      result.add(new Error(each.getMessage()));
    }

    return result;
  }
}
