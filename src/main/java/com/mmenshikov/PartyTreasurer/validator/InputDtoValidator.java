package com.mmenshikov.PartyTreasurer.validator;

import com.mmenshikov.PartyTreasurer.domain.dto.InputDto;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.util.CollectionUtils;

/**
 * Created: 10.08.2022 11:32
 *
 * @author MMenshikov
 */
public class InputDtoValidator implements ConstraintValidator<InputDtoConstraint, InputDto> {

  @Override
  public boolean isValid(InputDto dto, ConstraintValidatorContext constraintValidatorContext) {
    constraintValidatorContext.disableDefaultConstraintViolation();
    if (CollectionUtils.isEmpty(dto.getUses()) ||
        CollectionUtils.isEmpty(dto.getPurchases()) ||
        CollectionUtils.isEmpty(dto.getProducts()) ||
        CollectionUtils.isEmpty(dto.getUses())) {
      constraintValidatorContext.buildConstraintViolationWithTemplate(
              "invalid dto")
          .addPropertyNode("")
          .addConstraintViolation();
      return false;
    }
    var invalidUsersInPurchases = dto.getPurchases()
        .entrySet()
        .stream()
        .filter(entry ->
            dto.getParticipants().stream().noneMatch(p -> p.equals(entry.getKey()))
        ).findAny();

    invalidUsersInPurchases.ifPresent(
        entry -> constraintValidatorContext.buildConstraintViolationWithTemplate(
                "participant " + entry.getKey() + " not valid")
            .addPropertyNode("purchases")
            .addConstraintViolation());

    var invalidProductsInPurchases = dto.getPurchases().entrySet()
        .stream()
        .flatMap(entry -> entry.getValue().stream())
        .filter(
            s -> dto.getProducts().stream().noneMatch(product -> product.getName().equals(s)))
        .findAny();

    invalidProductsInPurchases.ifPresent(
        entry -> constraintValidatorContext.buildConstraintViolationWithTemplate(
                "product " + entry + " not valid")
            .addPropertyNode("purchases")
            .addConstraintViolation());

    var invalidUsersInUses = dto.getUses()
        .entrySet()
        .stream()
        .filter(entry ->
            dto.getParticipants().stream().noneMatch(p -> p.equals(entry.getKey()))
        ).findAny();

    invalidUsersInUses.ifPresent(
        entry -> constraintValidatorContext.buildConstraintViolationWithTemplate(
                "participant " + entry.getKey() + " not valid")
            .addPropertyNode("uses")
            .addConstraintViolation());

    var invalidProductsInUses = dto.getUses().entrySet()
        .stream()
        .flatMap(entry -> entry.getValue().stream())
        .filter(
            s -> dto.getProducts().stream().noneMatch(product -> product.getName().equals(s)))
        .findAny();

    invalidProductsInUses.ifPresent(
        entry -> constraintValidatorContext.buildConstraintViolationWithTemplate(
                "product " + entry + " not valid")
            .addPropertyNode("uses")
            .addConstraintViolation());

    return invalidProductsInPurchases.isEmpty() && invalidProductsInUses.isEmpty()
        && invalidUsersInPurchases.isEmpty()
        && invalidUsersInUses.isEmpty();
  }
}
