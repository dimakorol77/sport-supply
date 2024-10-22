Техническое задание для интернет-магазина спортивного питания
1. Введение
1.1. Цель документа
Данный документ представляет собой техническое задание для разработки бэкенда на Java Spring для интернет-магазина спортивного питания. ТЗ предназначено для команды студентов бэкенд-разработчиков итогового проекта в IT-школе. Основная цель проекта — создание функционального и безопасного приложения, отражающего реальные потребности интернет-магазина, с использованием изученных технологий и инструментов.
2. Общее описание
2.1. Обзор
Приложение позволяет клиентам выбирать товары спортивного питания из каталога, добавлять их в корзину, оформлять заказы и отслеживать их статус в режиме реального времени. Для администраторов приложение предоставляет инструменты для управления каталогом товаров, заказами, акциями и аналитикой продаж.
2.2. Функции продукта
- Система авторизации и учета пользователей: регистрация, аутентификация и управление учетными записями пользователей.
- Управление каталогом продуктов: добавление, редактирование и удаление товаров в каталоге.
- Корзина покупок и оформление заказов: возможность добавления товара в корзину с последующей покупкой (без реальной оплаты).
- История покупок и отслеживание заказов.
- Управление скидками и акциями: возможность указывать скидочную цену на товар и устанавливать акционные предложения.
2.3. Классы пользователей и характеристики
- Клиенты: конечные пользователи приложения, желающие купить товары спортивного питания.
- Администраторы: администраторы и менеджеры онлайн-магазина, использующие систему для управления каталогом товаров, заказами и акциями.
3. Требования к функциональности
3.1. Управление учетными записями пользователей
Система должна предоставлять возможности для регистрации, аутентификации и управления учетными записями пользователей:
- Регистрация новых пользователей с базовой информацией:
- Имя
- Email
- Номер телефона
- Пароль
- Данные должны проходить валидацию (например, проверка формата email, длины пароля).
- Аутентификация пользователей с использованием email и пароля.
- Редактирование профиля пользователя:
- Изменение имени
- Обновление контактной информации
- Смена пароля
- Удаление учетной записи по запросу пользователя.
3.2. Управление каталогом товаров
Администраторы должны иметь возможность управлять каталогом товаров:
- Добавление новых товаров с указанием:
- Названия
- Описания
- Цены
- Цены со скидкой (если применимо)
- Категории
- Изображения товара
- Редактирование существующих товаров.
- Удаление товаров из каталога.
- Управление категориями товаров:
- Добавление новых категорий
- Редактирование существующих категорий
- Удаление категорий
Категории товаров спортивного питания:
1. Протеины
   - Сывороточный протеин
   - Казеиновый протеин
   - Соевый протеин
   - Комплексные протеины
2. Аминокислоты
   - BCAA (лейцин, изолейцин, валин)
   - Глютамин
   - Аргинин
   - Комплексные аминокислоты
3. Гейнеры
   - Высокоуглеводные гейнеры
   - Низкоуглеводные гейнеры
4. Жиросжигатели
   - Термогеники
   - Липотропики
   - Блокаторы аппетита
5. Предтренировочные комплексы
   - С кофеином
   - Без кофеина
6. Креатин
   - Креатин моногидрат
   - Креатин гидрохлорид
   - Комплексные формы креатина
7. Витамины и минералы
   - Мультивитаминные комплексы
   - Омега-3 жирные кислоты
   - Антиоксиданты
8. Спортивные батончики и питание
   - Протеиновые батончики
   - Энергетические гели
   - Заменители питания
9. Изотоники и энергетики
   - Изотонические напитки
   - Энергетические напитки
10. Протеиновые добавки
    - Протеиновые порошки
    - Протеиновые напитки
11. Аксессуары
    - Шейкеры и бутылки
    - Спортивные сумки
    - Мерные ложки
12. Скидки и акции
3.3. Фильтрация и сортировка товаров на сайте
Пользователь должен иметь возможность:
- Фильтровать товары по:
- Цене (мин - макс)
- Наличию скидки
- Категории
- Бренду
- Типу протеина (для категории "Протеины")
- По группе витаминов (A, B, C, D)
- Форме выпуска (порошок, капсулы, жидкость)
- Сортировать товары по:
- Названию (по алфавиту в обе стороны)
- Цене (по возрастанию/убыванию)
- Дате добавления товара
- Рейтингу товара
- Популярности (количество продаж)
3.4. Система заказов
Система должна предоставлять функциональность для оформления и управления заказами:
Корзина покупок:
  - Добавление  товара из каталога в корзину
  - Управление количеством товаров в корзине
  - Просмотр содержимого корзины
 Оформление заказа:
  - Выбор способа доставки:
    - Доставка курьером
    - Самовывоз
  - Ввод адреса доставки и контактных данных
  - Подтверждение заказа
 Просмотр статуса заказа:
  - Отображение текущего статуса заказа
  - Обновление статуса в режиме реального времени
 Отмена заказа:
  - Пользователь может отменить заказ, если он находится в определенных статусах
 Просмотр истории заказов:
  - Список предыдущих заказов с деталями
Система доставки должна быть реализована с использованием триггера, который каждые 30 секунд меняет статус заказа на следующий:
Список статусов заказа:
  - Ожидает оплаты: заказ создан и ожидает оплаты
  - Оплачен: заказ оплачен
  - В обработке: заказ собирается на складе
  - В пути: заказ передан в службу доставки
  - Доставлено: заказ доставлен клиенту (конечный статус)
  - Отменено: заказ отменен пользователем или администратором
Примечание: Статус "Отменено" может быть присвоен только если заказ находится в статусах "Ожидает оплаты" или "Оплачен".
3.5. Система оплаты
- Реальная интеграция с платежными системами не требуется.
- При нажатии на кнопку "Купить" информация о покупке отправляется в систему, а из корзины удаляются купленные товары.
- История покупок пополняется данными о купленных товарах:
  - Название
  - Описание
  - Фото
  - Количество
  - Стоимость
  - Категория
3.6. Управление акциями и скидками
- Возможность указать скидочную цену на товар.
- Товар дня:
  - Автоматическое определение товара с наибольшей скидкой
  - Если несколько товаров имеют одинаковую максимальную скидку, выбор случайным образом из этого списка
- Управление акциями:
  - Создание и управление акционными предложениями
  - Установка сроков действия акций
3.7. Отчетность
Необходимо реализовать функционал отчетности для администраторов:
- Топ 10 купленных товаров.
- Топ 10 часто отменяемых товаров.
- Список товаров, которые находятся в статусе "Ожидает оплаты" более N дней.
- Прибыль за выбранный период с возможностью группировки по:
  - Часам
  - Дням
  - Неделям
  - Месяцам
3.8. Избранные товары
- Пользователи могут добавлять товары в избранное.
- Просмотр списка избранных товаров в личном кабинете.
4. Требования к технологическому стеку
 4.1. Язык программирования и фреймворки
- Язык программирования: Java 11 или выше.
- Фреймворк: Spring Boot 2.x.
- Безопасность: Spring Security с использованием JWT для аутентификации и авторизации.
- ORM: Spring Data JPA/Hibernate.
- База данных: MySQL.
- Миграции базы данных: Flyway или Liquibase.
- Контейнеризация: Docker для приложения и базы данных.
- Документация API: Swagger/OpenAPI.
- Тестирование: JUnit, Mockito.
5. Требования к интерфейсу
5.1. Внешние интерфейсы (API для фронтенда)
- RESTful API: Поддержка основных HTTP методов (GET, POST, PUT, DELETE).
- Формат данных: JSON.
- Аутентификация и авторизация: Использование JWT для обеспечения безопасности данных пользователей.
- Документация API: Подробное описание всех эндпоинтов, параметров запросов и ответов с использованием Swagger.
5.2. Пользовательский интерфейс (опционально)
- Фронтенд: При необходимости, разработка простого интерфейса для демонстрации работы приложения.
6. Требования к качеству
6.1. Надежность
- Обработка ошибок и исключений: Все исключительные ситуации должны быть обработаны с информированием пользователя о возникших проблемах.
6.2. Безопасность
- Шифрование данных: Пароли пользователей должны храниться в зашифрованном виде.
- Валидация данных: Все входящие данные должны быть проверены на корректность.
- Защита от уязвимостей: Предотвращение SQL-инъекций, XSS и других распространенных атак.
6.3. Производительность
- Оптимизация запросов к базе данных.
- Кэширование: При необходимости использовать кэширование для ускорения доступа к часто запрашиваемым данным.
7. Требования к разработке
7.1. Стандарты кодирования
- Читаемый и поддерживаемый код: Следование общепринятым стилям кодирования.
- Документирование кода: Комментарии в коде для ключевых функций и методов.
7.2. Управление версиями
- Использование Git: Регулярные коммиты с информативными сообщениями.
- Структура репозитория:
  - Создание отдельных веток для каждой задачи.
  - Использование pull-request'ов и их ревью другими участниками команды.
- .gitignore: Добавление всех ненужных для версионирования файлов и папок.
7.3. Управление проектом
- Система трекинга задач: Использование Jira или аналогичной системы.
- Разбиение проекта на спринты: Четкое планирование задач по этапам.
- Постановка задач: Четкие и детализированные описания задач с критериями приемки.
- Отслеживание прогресса: Регулярное обновление статусов задач.
8. Тестирование
- Unit-тесты: Покрытие основных функций приложения.
- Интеграционные тесты: Проверка взаимодействия между модулями и с базой данных.
- Тестирование безопасности: Проверка правильности реализации аутентификации и авторизации.
- Нагрузочное тестирование: Оценка производительности приложения под нагрузкой (опционально).
9. Развертывание
- Docker Compose: Создание файла `docker-compose.yml` для запуска всех компонентов системы.
- Инструкции по развертыванию: Подробное описание шагов для запуска приложения в README.md.
10. Документация
- README.md:
  - Описание проекта.
  - Инструкции по сборке и запуску.
  - Ссылки на используемые технологии.
  - API документация: Доступна через Swagger UI.
  - Схема базы данных: Диаграмма сущностей и связей (ER-диаграмма).
11. Критерии оценивания
11.1. Функциональность
1. Регистрация пользователя
2. Аутентификация пользователя
3. Редактирование профиля пользователя
4. Удаление учетной записи
5. Добавление нового товара
6. Редактирование товара
7. Удаление товара
8. Добавление новой категории товаров
9. Редактирование категории товаров
10. Удаление категории товаров
11. Получение списка товаров
12. Получение детальной информации о товаре
13. Получение списка категорий товаров
14. Добавление товара в корзину
15. Оформление заказа
16. Просмотр статуса заказа
17. История покупок пользователя
18. Получение списка избранных товаров пользователя
11.2. Качество кода
- Читаемость и понятность кода.
- Следование принципам DRY (Don't Repeat Yourself) и разделения ответственности.
- Использование паттернов проектирования (где применимо).
- Адекватная обработка исключений.
- Понятные и информативные имена переменных, функций и классов.
- Структурированность и модульность кода.
11.3. API и документация
- Соответствие RESTful API стандартам.
- Подробная документация API с использованием Swagger.
11.4. Безопасность
- Реализация аутентификации и авторизации через Spring Security и JWT.
- Защита приложения от распространенных уязвимостей.
11.5. Работа с Git
- Регулярные и информативные коммиты.
- Использование отдельных веток для задач.
- Проведение код-ревью и одобрение pull-request'ов другими членами команды.
- Правильное использование файла `.gitignore`.
11.6. Управление задачами
- Все задачи занесены в систему трекинга.
- Задачи разделены по спринтам.
- Четкое и детальное описание задач.
- Актуальные статусы задач.
11.7. Командная работа
- Регулярные командные встречи с протоколированием.
- Уважительное и конструктивное общение внутри команды.
- Информативные презентации на общих встречах.
12. План разработки по спринтам
Спринт 1: Управление учетными записями пользователей
- Создание структуры базы данных.
- Настройка проекта и репозитория.
- Реализация регистрации и аутентификации пользователей.
- Реализация функционала добавления и просмотра категорий.
- Документация API (Swagger).
Спринт 2: Работа с каталогом товаров и навигация по сайту
- Реализация добавления, редактирования и удаления категорий.
- Реализация добавления, редактирования и удаления товаров.
- Создание API для отображения товаров с фильтрацией и сортировкой.
- Реализация функционала избранных товаров.
Спринт 3: Система заказов и оплаты
- Реализация корзины покупок.
- Оформление заказа с выбором способа доставки.
- Симуляция процесса оплаты (заглушка).
- Реализация системы статусов заказа с автоматическим обновлением.
Спринт 4: Управление отчетностью
- Реализация функционала отчетности для администраторов.
- Тестирование и отладка приложения.
- Связь бэкенда с фронтендом (если фронтенд предоставляется).
- Подготовка документации и презентации проекта.
13. Авторы проекта



 14. Стек технологий
- Java 11+ — основной язык программирования.
- Spring Boot 2.x — фреймворк для создания приложения.
- Spring Security — обеспечение безопасности приложения.
- Spring Data JPA/Hibernate — работа с базой данных.
- MySQL— реляционная база данных.
- Flyway/Liquibase — миграции базы данных.
- JWT (JSON Web Tokens) — аутентификация и авторизация.
- Swagger/OpenAPI — документация API.
- Docker — контейнеризация приложения и базы данных.
- JUnit, Mockito — тестирование приложения.

 15. Заключение
Данный проект предоставляет возможность студентам применить на практике изученные технологии и получить опыт командной разработки реального веб-приложения. Соблюдение данного технического задания и активное участие каждого члена команды обеспечит успешную реализацию проекта и его защиту в IT-школе.
---

Ссылки:

- Репозиторий проекта: [GitHub URL]
- Документация API: [Swagger URL]
- Презентация команды: [Presentation URL]



