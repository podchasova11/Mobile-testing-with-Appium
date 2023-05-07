## Зачем взаимодействовать с файлами напрямую?

- [ ] Заполните приложение данными пользователя
- [ ] Проверка внутренних сохраненных данных приложения
- [ ] Загружайте фотографии/медиафайлы на устройство быстро и надежно

Мобильные устройства — это маленькие компьютеры, а значит, у них есть собственные файловые системы. Пользователи устройств обычно не взаимодействуют напрямую с файловой системой, но иногда это может быть удобно при написании автоматизированных тестов. Давайте взглянем на набор API Appium, доступный для этого. Если пользователи не могут напрямую взаимодействовать с файловой системой устройства, зачем мне делать это для автоматизированного теста? На самом деле, чтобы облегчить мне жизнь. 

- [ ] Прежде всего, если мое приложение сохраняет данные в дисковом хранилище устройства как способ управления своим состоянием, я мог бы предварительно заполнить устройство некоторыми данными такого рода, чтобы перевести приложение в известное состояние, прежде чем я начну свой тест. Это может помочь сделать настройку возможной или просто менее утомительной. 

- [ ] Во-вторых, я могу по существу сделать обратное. Взаимодействуйте с приложением из пользовательского интерфейса, а затем используйте файловый API Appium, чтобы посмотреть на результаты того, что мое приложение сделало с этими взаимодействиями на диске. Это может подтвердить, что приложение правильно сохраняет свои данные. 

- [ ] Наконец, файловый API — это хороший способ загрузить фотографии и другие медиафайлы на устройство . Особенно при работе с симуляторами, где файловый API актуален. Например, фотографировать будет сложно. Особенно, если им нужно соответствовать чему-то, что мой тест ожидает позже. 

Было бы неплохо управлять файлами на устройстве из кода, а не нажимая кнопки. Например, предположим, что я хочу начать свою тестовую сессию с некоторых конкретных фотографий в фотопленке, потому что я хочу, чтобы мое приложение имело доступ к изображениям, о которых я знаю заранее. Или, может быть, приложение записывает какие-то файлы в файловую систему устройства в ходе своей работы, и я хочу убедиться, что они записаны правильно, или прочитать их для какой-то другой цели. Это то, что я могу делать с помощью файловых API Appium.

Файловый ##**API Appium**## очень прост и состоит из двух основных команд. 

1. Первая команда называется pushFile, и это то, что позволяет мне взять файл на моем компьютере локально для сервера Appium и отправить его в файловую систему на устройстве. Первый параметр — это путь, по которому я хочу отправить файл на устройстве. Второй параметр — файловый объект, представляющий файл, который я хочу отправить. Appium позаботится о чтении этого файла на моем компьютере, когда я запускаю эту команду. 

 ``` put a file on the device from the computer disk
 driver.pushFile(“/path/on/device”, new File(“/path/on/disk”));
 ```
 
2. Вторая команда называется pullFile. Он принимает только один аргумент — путь к файлу в файловой системе устройства, который я хочу загрузить. Результатом выполнения команды является массив байтов в Java-клиенте Appium. С помощью этого массива байтов я мог выполнить некоторый анализ содержимого файла в своем сценарии или записать файл на диск для последующего просмотра. 

 ``` download a file from the device to the test script
 byte[] fileData = driver.pullFile(“/path/on/device”);
 ```
Есть несколько ограничений для этого API, о которых следует помнить, когда я думаю об его использовании, которые связаны с тем, где мне разрешено размещать файлы или извлекать файлы. 

- [ ] В iOS приложения изолированы в файловой системе в своих собственных контейнерах, и я не могу получить доступ к основным системным файлам. Другими словами, я ограничен работой в контейнере приложения.
- [ ] На Android у меня есть полный root-доступ, но только в том случае, если я использую эмулятор или  если устройство рутировано . 


Вот пример кода Java для файлового API Appium:

![image](https://user-images.githubusercontent.com/16668925/222891196-704f459a-461a-4a5f-8c85-ce37884cec5f.png)


В Python аналогия будет следующей:

- [ ] Во-первых, у меня есть возможность создать файл на выбор. Команда для этого driver.push_fileпринимает 2 параметра.
Первый — это путь на пути, где должен быть создан файл. В Android это должен быть абсолютный путь в файловой системе устройства. Обратите внимание, что тестер должен иметь права на запись по этому пути. Поэтому я не могу писать в некоторых областях физического устройства, если оно не рутировано. Но я могу писать всю файловую систему эмулятора. А на iOS ограничения безопасности еще более строгие. По сути, приложение может записывать файлы в свою собственную песочницу, называемую контейнером приложения. Поэтому, когда я указываю пути для этой команды в iOS, все мои пути осуществляются к контейнеру приложений для моего тестируемого приложения.

- [ ] Второй параметр — это сами данные файла, которые я хочу сохранить. Всегда загружаются файлы двоичных данных и двоичные данные, которые не могут быть отправлены на сервер Appium как часть HTTP-запроса, мне нужно кодировать данные файла как файл Base-64. Мне не нужно больше ничего знать о кодировке Base-64, за исключением того, что этот способ использует общедоступные двоичные данные и приводит их в виде текста, который можно безопасно отправить в HTTP-запросе. Существуют встроенные функции Python, которые помогают преобразовать любые данные в структуру кодировки Base-64.Конечно, думаю, что мне нужно отправить данные сюда файла, если то, что я действительно могу сделать, это переместить файл из моей файловой системы в файловую систему, мне сначала нужно проверить локальный файл и получить его данные перед их кодированием и отправкой в Аппиум. Так что push_file, поиск для загрузки файлов на устройство.

