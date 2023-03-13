package cz.cvut.fit.ruiansearch.repository;

import cz.cvut.fit.ruiansearch.model.EdisMaxQuery;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.solr.core.DefaultQueryParser;
import org.springframework.data.solr.core.QueryParserBase;
import org.springframework.data.solr.core.mapping.SolrPersistentEntity;
import org.springframework.data.solr.core.mapping.SolrPersistentProperty;
import org.springframework.data.solr.core.query.AbstractQueryDecorator;
import org.springframework.data.solr.core.query.SolrDataQuery;

public class EdisMaxQueryParser extends QueryParserBase<SolrDataQuery> {
    private final DefaultQueryParser defaultQueryParser;

    public EdisMaxQueryParser(MappingContext<? extends SolrPersistentEntity<?>, SolrPersistentProperty> mappingContext) {
        super(mappingContext);
        defaultQueryParser = new DefaultQueryParser(mappingContext);
    }

    @Override
    public SolrQuery doConstructSolrQuery(SolrDataQuery edismaxQuery, Class<?> domainType) {
        // use defaultQueryParser to populate basic query parameters
        SolrQuery solrQuery = defaultQueryParser.doConstructSolrQuery(edismaxQuery, domainType);

        EdisMaxQuery query = null;
        // for some reason the API wrapped our query object with NamedObjectsQuery, so we need to unwrapped/get our actual query object first
        if(edismaxQuery instanceof EdisMaxQuery)
            query = (EdisMaxQuery) edismaxQuery;
        else if(edismaxQuery instanceof AbstractQueryDecorator)
            query = ((EdisMaxQuery)((AbstractQueryDecorator)edismaxQuery).getDecoratedQuery());
        else
            return solrQuery;

        solrQuery.add("df", "Text_adresy");
        solrQuery.add("qf", query.getQueryField());

        return solrQuery;
    }
}
