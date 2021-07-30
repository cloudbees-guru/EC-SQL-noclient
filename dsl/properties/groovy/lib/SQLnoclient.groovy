import com.cloudbees.cd.plugin.sql.DatabaseSelector
import com.cloudbees.cd.plugin.sql.QueryExecutor
import com.cloudbees.cd.plugin.sql.connections.DatabaseConnection
import com.cloudbees.flowpdf.*

import java.sql.Connection

/**
* SQLnoclient
*/
class SQLnoclient extends FlowPlugin {

    @Override
    Map<String, Object> pluginInfo() {
        return [
                pluginName     : '@PLUGIN_KEY@',
                pluginVersion  : '@PLUGIN_VERSION@',
                configFields   : ['config'],
                configLocations: ['ec_plugin_cfgs'],
                defaultConfigValues: [:]
        ]
    }
/** This is a special method for checking connection during configuration creation
    */
    def checkConnection(StepParameters p, StepResult sr) {
        // Use this pre-defined method to check connection parameters
        try {
            // Put some checks here
            def config = context.configValues
            log.info(config)
            // Getting parameters:
            // log.info config.asMap.get('config')
            // log.info config.asMap.get('desc')
            
            // assert config.getRequiredCredential("credential").secretValue == "secret"
        }  catch (Throwable e) {
            // Set this property to show the error in the UI
            sr.setOutcomeProperty("/myJob/configError", e.message + System.lineSeparator() + "Please change the code of checkConnection method to incorporate your own connection checking logic")
            sr.apply()
            throw e
        }
    }
// === check connection ends ===
/**
    * executeQuery - execute Query/execute Query
    * Add your code into this method and it will be called when the step runs
    * @param database (required: true)
    * @param server (required: true)
    * @param port (required: true)
    * @param credential (required: true)
    * @param databaseName (required: true)
    * @param properties (required: false)
    * @param sql_query (required: true)
    
    */
    def executeQuery(StepParameters p, StepResult sr) {
        // Use this parameters wrapper for convenient access to your parameters
        ExecuteQueryParameters sp = ExecuteQueryParameters.initParameters(p)

        DatabaseSelector databaseSelector = new DatabaseSelector()
        DatabaseConnection databaseConnection = databaseSelector.getDatabaseConnection(
                p.asMap.get('database').toString(),
                p.asMap.get('server').toString(),
                p.asMap.get('port').toString(),
                p.asMap.get('databaseName').toString(),
                p.asMap.get('properties').toString(),
                ((Credential)p.asMap.get('credential')).getUserName(),
                ((Credential)p.asMap.get('credential')).getSecretValue()
        )
        Connection connection = databaseConnection.connect(false)
        QueryExecutor queryExecutor = new QueryExecutor(databaseConnection)

        log.info "Executing query:"
        queryExecutor.executeQuery(p.asMap.get('sql_query').toString(), false)
        log.info "Query was executed"

        databaseConnection.disconnect(connection)

        sr.apply()
        log.info("step execute Query has been finished")
    }

/**
    * executeQueries - execute Queries/execute Queries
    * Add your code into this method and it will be called when the step runs
    * @param database (required: true)
    * @param server (required: true)
    * @param port (required: true)
    * @param credential (required: true)
    * @param databaseName (required: true)
    * @param properties (required: false)
    * @param sql_queries (required: true)
    
    */
    def executeQueries(StepParameters p, StepResult sr) {
        // Use this parameters wrapper for convenient access to your parameters
        ExecuteQueriesParameters sp = ExecuteQueriesParameters.initParameters(p)

        DatabaseSelector databaseSelector = new DatabaseSelector()
        DatabaseConnection databaseConnection = databaseSelector.getDatabaseConnection(
                p.asMap.get('database').toString(),
                p.asMap.get('server').toString(),
                p.asMap.get('port').toString(),
                p.asMap.get('databaseName').toString(),
                p.asMap.get('properties').toString(),
                ((Credential)p.asMap.get('credential')).getUserName(),
                ((Credential)p.asMap.get('credential')).getSecretValue()
        )
        Connection connection = databaseConnection.connect(false)
        QueryExecutor queryExecutor = new QueryExecutor(databaseConnection)

        log.info "Executing query:"
        queryExecutor.executeQueries(p.asMap.get('sql_queries').toString(), false)
        log.info "Query was executed"

        databaseConnection.disconnect(connection)

        sr.apply()
        log.info("step execute Queries has been finished")
    }

// === step ends ===

}