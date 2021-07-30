$[/myProject/groovy/scripts/preamble.groovy.ignore]

SQLnoclient plugin = new SQLnoclient()
plugin.runStep('$[/myProcedure/name]', 'checkConnection', 'checkConnection')