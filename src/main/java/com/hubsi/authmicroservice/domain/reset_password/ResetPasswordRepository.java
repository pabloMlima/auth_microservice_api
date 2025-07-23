package com.hubsi.authmicroservice.domain.reset_password;

import java.util.Optional;

public interface ResetPasswordRepository {

    ResetPassword save(ResetPassword resetPassword);

    Optional<ResetPassword> findByToken(String token);

}
