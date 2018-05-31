package ma.maman.jeanne.config;

import io.github.jhipster.config.JHipsterProperties;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(Expirations.timeToLiveExpiration(Duration.of(ehcache.getTimeToLiveSeconds(), TimeUnit.SECONDS)))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(ma.maman.jeanne.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.SocialUserConnection.class.getName(), jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.Company.class.getName(), jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.Company.class.getName() + ".cars", jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.Company.class.getName() + ".trains", jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.Company.class.getName() + ".itineraires", jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.Company.class.getName() + ".companyClasses", jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.Company.class.getName() + ".feesAgencies", jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.Segment.class.getName(), jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.Itineraire.class.getName(), jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.Itineraire.class.getName() + ".voyages", jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.Car.class.getName(), jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.Car.class.getName() + ".voyages", jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.Train.class.getName(), jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.Train.class.getName() + ".wagons", jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.Train.class.getName() + ".voyages", jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.Wagon.class.getName(), jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.ModelCar.class.getName(), jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.ModelCar.class.getName() + ".cars", jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.ModelTrain.class.getName(), jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.ModelTrain.class.getName() + ".wagons", jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.City.class.getName(), jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.City.class.getName() + ".routes", jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.Fare.class.getName(), jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.Passenger.class.getName(), jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.Month.class.getName(), jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.Day.class.getName(), jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.Day.class.getName() + ".itineraires", jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.Email.class.getName(), jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.Voyage.class.getName(), jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.Voyage.class.getName() + ".segments", jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.Booking.class.getName(), jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.Booking.class.getName() + ".passengers", jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.Booking.class.getName() + ".fares", jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.Booking.class.getName() + ".emails", jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.Booking.class.getName() + ".segments", jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.Agency.class.getName(), jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.Agency.class.getName() + ".deposits", jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.Agency.class.getName() + ".feesAgencies", jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.Deposit.class.getName(), jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.Classe.class.getName(), jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.Classe.class.getName() + ".itineraires", jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.Classe.class.getName() + ".companyClasses", jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.CompanyClasse.class.getName(), jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.Routes.class.getName(), jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.Routes.class.getName() + ".itineraires", jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.Routes.class.getName() + ".voyages", jcacheConfiguration);
            cm.createCache(ma.maman.jeanne.domain.FeesAgency.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
