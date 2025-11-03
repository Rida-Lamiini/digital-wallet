package com.github.yildizmy.dto.mapper;

import com.github.yildizmy.domain.entity.Wallet;
import com.github.yildizmy.dto.request.WalletRequest;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-03T13:14:24+0000",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251023-0518, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class WalletRequestMapperImpl extends WalletRequestMapper {

    @Override
    public Wallet toWallet(WalletRequest dto) {
        if ( dto == null ) {
            return null;
        }

        Wallet wallet = new Wallet();

        wallet.setBalance( dto.getBalance() );
        wallet.setId( dto.getId() );

        wallet.setName( org.apache.commons.text.WordUtils.capitalizeFully(dto.getName()) );
        wallet.setIban( org.apache.commons.lang3.StringUtils.upperCase(dto.getIban()) );

        setToEntityFields( wallet, dto );

        return wallet;
    }

    @Override
    public WalletRequest toWalletRequest(Wallet entity) {
        if ( entity == null ) {
            return null;
        }

        WalletRequest walletRequest = new WalletRequest();

        walletRequest.setBalance( entity.getBalance() );
        walletRequest.setIban( entity.getIban() );
        walletRequest.setId( entity.getId() );
        walletRequest.setName( entity.getName() );

        return walletRequest;
    }
}
