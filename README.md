# Automação de Testes de Serviço com REST Assured

As ferramentas utilizadas são:

	• REST Assured
	• TestNG
	• Log4j2
	• Maven
	• jdk 1.8.0


Os testes foram separados em suites, para uma melhor divisão que poderá ser usada na execução:
                         
	• All suites
	• Contract
	• Functional
	• Negative
	• Healt check

Execução das Suites:

	• All suites: mvn test
	• Contract: mvn test -Dgroups="contract"
	• Functional: mvn test -Dgroups="functional"
	• Negative: mvn test -Dgroups="negative"
	• Healt check: mvn test -Dgroups="health_check"

--------------------------------------------------
Observações:

	• Os dados estão sendo informados via código e não buscados,
    a estrutura deve ter acesso a base de dados para realizar os testes corretamente
	• Não está sendo realizada a verificação da validade dos dados de simulação,
    deve existir essa verificação para preservar a integridade das informações
