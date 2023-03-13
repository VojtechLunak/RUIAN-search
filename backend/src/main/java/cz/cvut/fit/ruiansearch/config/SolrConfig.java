package cz.cvut.fit.ruiansearch.config;

import cz.cvut.fit.ruiansearch.model.EdisMaxQuery;
import cz.cvut.fit.ruiansearch.repository.EdisMaxQueryParser;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.mapping.SimpleSolrMappingContext;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

@Configuration
@EnableSolrRepositories("cz.cvut.fit.ruiansearch.repository")
public class SolrConfig {
    /*
     * Try to use Solr url defined in environment variable RUIAN_SOLR_URL
     * If it's not set, try to connect to solr on localhost on Solr's default port
     */
    @Value("${RUIAN_SOLR_URL:http://localhost:8983/solr}")
    private String solrUrl;

    @Bean
    public HttpSolrClient solrClient() {
        return new HttpSolrClient.Builder(solrUrl).build();
    }

    @Bean
    public SolrTemplate solrTemplate(SolrClient client) {
        SolrTemplate template = new SolrTemplate(client) {
            @Override
            public void afterPropertiesSet() {
                super.afterPropertiesSet();
                registerQueryParser(EdisMaxQuery.class, new EdisMaxQueryParser(new SimpleSolrMappingContext()));
            }
        };

        template.afterPropertiesSet();
        return template;
    }
}
