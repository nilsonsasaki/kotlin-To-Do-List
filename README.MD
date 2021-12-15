# Kotlin to do List

Esse aplicativo foi desenvoldido primeiramente como solução do trabalho final do Santander Bootcamp | Mobile Developer, onde o aplicativo era bem simples, sem a utilização de fragmentos ou banco de dados SQLite, contudo após o termino do bootcamp continuo desenvolvendo o aplicativo de forma a praticar e aprimorar os conhecimentos

# Introdução

O Kotlin to do List é um aplicativo de lembretes de Tarefas onde o usuário pode adicionar, remover e editar tarefas que possui uma Activity que é host para três fragmentos, onde um fragmento é a lista de tarefas, um fragmento mostra os detalhes da tarefa e um fragmento é responsável por criar ou editar tarefas.

Pode-se navegar da lista de tarefas para os detalhes de uma tarefa tocando-a,filtrar os resultados para o dia atual tocaldo o texto All Tasks, além disso pode-se usar o FloatingActionButton para navegar para o fragmento de criação de tarefas.

No fragmento de detalhes da tarefa específica pode-se deletar a tarefa através de um botão ou edita-la pelo Floating ActionButton

O fragmento de criação de tarefas, pode-se inserir strings para o Título, utilizar o datePicker e timePicker para a data e horário além de escolher a prioridade em um dropdown menu

Foram implementados validações para verificar se o titulo esta em branco, se a data e horário são posteriores aos valores atuais, além disso o tempo de término deve sedr maior que a data de início

# Aprendizado

Para o desenvolvimento do app foram utilizados:

1. O componente Room e corrotinas, para salvar os dados em um banco de dados
2. O componentes Navigation, para navegar entre os três fragmentos que são hosteados em uma única Activity;
3. O ViewBinding, para acessar os componentes de layout de forma mais fácil e código mais limpo 
4. Um viewModel compartilhado entre os três fragmentos para que os valores não sejam perdidos por causa do ciclo de vida do aplicativo
5. DatePicker/TimePicker além do Dropdown menu

# O que poderia ser desenvolvido:

1. Aprimorar o Layout e tema
2. Configurar o comportamento de todos os componentes de forma a tornar a experiência do usiário melhor
3. Funcionalidades como mostrar lembretes , definir local da tarefa, repetição da tarefa entre outros
