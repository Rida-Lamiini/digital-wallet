package com.github.yildizmy.dto.mapper;

import com.github.yildizmy.domain.entity.Role;
import com.github.yildizmy.domain.entity.User;
import com.github.yildizmy.domain.entity.Wallet;
import com.github.yildizmy.dto.response.RoleResponse;
import com.github.yildizmy.dto.response.UserResponse;
import com.github.yildizmy.dto.response.WalletResponse;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-03T13:14:24+0000",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251023-0518, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class WalletResponseMapperImpl implements WalletResponseMapper {

    @Override
    public Wallet toWallet(WalletResponse dto) {
        if ( dto == null ) {
            return null;
        }

        Wallet wallet = new Wallet();

        wallet.setBalance( dto.getBalance() );
        wallet.setIban( dto.getIban() );
        wallet.setId( dto.getId() );
        wallet.setName( dto.getName() );
        wallet.setUser( userResponseToUser( dto.getUser() ) );

        return wallet;
    }

    @Override
    public WalletResponse toWalletResponse(Wallet entity) {
        if ( entity == null ) {
            return null;
        }

        WalletResponse walletResponse = new WalletResponse();

        walletResponse.setBalance( entity.getBalance() );
        walletResponse.setIban( entity.getIban() );
        walletResponse.setId( entity.getId() );
        walletResponse.setName( entity.getName() );
        walletResponse.setUser( userToUserResponse( entity.getUser() ) );

        return walletResponse;
    }

    protected Role roleResponseToRole(RoleResponse roleResponse) {
        if ( roleResponse == null ) {
            return null;
        }

        Role role = new Role();

        role.setId( roleResponse.getId() );
        role.setType( roleResponse.getType() );

        return role;
    }

    protected Set<Role> roleResponseSetToRoleSet(Set<RoleResponse> set) {
        if ( set == null ) {
            return null;
        }

        Set<Role> set1 = new LinkedHashSet<Role>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( RoleResponse roleResponse : set ) {
            set1.add( roleResponseToRole( roleResponse ) );
        }

        return set1;
    }

    protected User userResponseToUser(UserResponse userResponse) {
        if ( userResponse == null ) {
            return null;
        }

        User user = new User();

        user.setEmail( userResponse.getEmail() );
        user.setFirstName( userResponse.getFirstName() );
        user.setId( userResponse.getId() );
        user.setLastName( userResponse.getLastName() );
        user.setRoles( roleResponseSetToRoleSet( userResponse.getRoles() ) );
        user.setUsername( userResponse.getUsername() );

        return user;
    }

    protected RoleResponse roleToRoleResponse(Role role) {
        if ( role == null ) {
            return null;
        }

        RoleResponse roleResponse = new RoleResponse();

        roleResponse.setId( role.getId() );
        roleResponse.setType( role.getType() );

        return roleResponse;
    }

    protected Set<RoleResponse> roleSetToRoleResponseSet(Set<Role> set) {
        if ( set == null ) {
            return null;
        }

        Set<RoleResponse> set1 = new LinkedHashSet<RoleResponse>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( Role role : set ) {
            set1.add( roleToRoleResponse( role ) );
        }

        return set1;
    }

    protected UserResponse userToUserResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponse userResponse = new UserResponse();

        userResponse.setEmail( user.getEmail() );
        userResponse.setFirstName( user.getFirstName() );
        userResponse.setId( user.getId() );
        userResponse.setLastName( user.getLastName() );
        userResponse.setRoles( roleSetToRoleResponseSet( user.getRoles() ) );
        userResponse.setUsername( user.getUsername() );

        setFullName( userResponse, user );

        return userResponse;
    }
}
