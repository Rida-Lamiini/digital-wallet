package com.github.yildizmy.dto.mapper;

import com.github.yildizmy.domain.entity.Role;
import com.github.yildizmy.domain.entity.Transaction;
import com.github.yildizmy.domain.entity.Type;
import com.github.yildizmy.domain.entity.User;
import com.github.yildizmy.domain.entity.Wallet;
import com.github.yildizmy.dto.response.RoleResponse;
import com.github.yildizmy.dto.response.TransactionResponse;
import com.github.yildizmy.dto.response.TypeResponse;
import com.github.yildizmy.dto.response.UserResponse;
import com.github.yildizmy.dto.response.WalletResponse;
import java.time.Instant;
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
public class TransactionResponseMapperImpl implements TransactionResponseMapper {

    @Override
    public Transaction toTransaction(TransactionResponse dto) {
        if ( dto == null ) {
            return null;
        }

        Transaction transaction = new Transaction();

        transaction.setAmount( dto.getAmount() );
        if ( dto.getCreatedAt() != null ) {
            transaction.setCreatedAt( Instant.parse( dto.getCreatedAt() ) );
        }
        transaction.setDescription( dto.getDescription() );
        transaction.setFromWallet( walletResponseToWallet( dto.getFromWallet() ) );
        transaction.setId( dto.getId() );
        transaction.setReferenceNumber( dto.getReferenceNumber() );
        transaction.setStatus( dto.getStatus() );
        transaction.setToWallet( walletResponseToWallet( dto.getToWallet() ) );
        transaction.setType( typeResponseToType( dto.getType() ) );

        return transaction;
    }

    @Override
    public TransactionResponse toTransactionResponse(Transaction entity) {
        if ( entity == null ) {
            return null;
        }

        TransactionResponse transactionResponse = new TransactionResponse();

        transactionResponse.setAmount( entity.getAmount() );
        transactionResponse.setDescription( entity.getDescription() );
        transactionResponse.setFromWallet( walletToWalletResponse( entity.getFromWallet() ) );
        transactionResponse.setId( entity.getId() );
        transactionResponse.setReferenceNumber( entity.getReferenceNumber() );
        transactionResponse.setStatus( entity.getStatus() );
        transactionResponse.setToWallet( walletToWalletResponse( entity.getToWallet() ) );
        transactionResponse.setType( typeToTypeResponse( entity.getType() ) );

        formatCreatedAt( transactionResponse, entity );

        return transactionResponse;
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

    protected Wallet walletResponseToWallet(WalletResponse walletResponse) {
        if ( walletResponse == null ) {
            return null;
        }

        Wallet wallet = new Wallet();

        wallet.setBalance( walletResponse.getBalance() );
        wallet.setIban( walletResponse.getIban() );
        wallet.setId( walletResponse.getId() );
        wallet.setName( walletResponse.getName() );
        wallet.setUser( userResponseToUser( walletResponse.getUser() ) );

        return wallet;
    }

    protected Type typeResponseToType(TypeResponse typeResponse) {
        if ( typeResponse == null ) {
            return null;
        }

        Type type = new Type();

        type.setDescription( typeResponse.getDescription() );
        type.setId( typeResponse.getId() );
        type.setName( typeResponse.getName() );

        return type;
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

        return userResponse;
    }

    protected WalletResponse walletToWalletResponse(Wallet wallet) {
        if ( wallet == null ) {
            return null;
        }

        WalletResponse walletResponse = new WalletResponse();

        walletResponse.setBalance( wallet.getBalance() );
        walletResponse.setIban( wallet.getIban() );
        walletResponse.setId( wallet.getId() );
        walletResponse.setName( wallet.getName() );
        walletResponse.setUser( userToUserResponse( wallet.getUser() ) );

        return walletResponse;
    }

    protected TypeResponse typeToTypeResponse(Type type) {
        if ( type == null ) {
            return null;
        }

        TypeResponse typeResponse = new TypeResponse();

        typeResponse.setDescription( type.getDescription() );
        typeResponse.setId( type.getId() );
        typeResponse.setName( type.getName() );

        return typeResponse;
    }
}
