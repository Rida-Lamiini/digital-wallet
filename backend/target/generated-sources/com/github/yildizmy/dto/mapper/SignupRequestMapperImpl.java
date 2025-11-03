package com.github.yildizmy.dto.mapper;

import com.github.yildizmy.domain.entity.User;
import com.github.yildizmy.dto.request.SignupRequest;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-03T13:14:23+0000",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251023-0518, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class SignupRequestMapperImpl extends SignupRequestMapper {

    @Override
    public User toUser(SignupRequest dto) {
        if ( dto == null ) {
            return null;
        }

        User user = new User();

        user.setId( dto.getId() );

        user.setFirstName( org.apache.commons.text.WordUtils.capitalizeFully(dto.getFirstName()) );
        user.setLastName( org.apache.commons.text.WordUtils.capitalizeFully(dto.getLastName()) );
        user.setUsername( dto.getUsername().trim().toLowerCase() );
        user.setEmail( dto.getEmail().trim().toLowerCase() );

        setToEntityFields( user, dto );

        return user;
    }
}
