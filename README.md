Дневник калорий
===============================

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/e222cbb50a3d4da5b6661863b632e0e2)](https://www.codacy.com/app/elusive935/topjava10?utm_source=github.com&utm_medium=referral&utm_content=elusive935/topjava10&utm_campaign=badger)

**Дневник для ведения записей о съеденных калориях**

Укажите количество съеденных калорий, дату и время, добавьте краткое название приема пищи и 
ваша запись будет надежно сохранена в базе данных.

![Add Meal](src/main/resources/screenshots/AddMeal.jpg?raw=true)
  
Вы можете легко управлять своими записями: добавлять, удалять и редактировать.
Дни, в которых сумма калорий превысит норму, приложение выделит красным цветом.

![Add Meal](src/main/resources/screenshots/TableMeal.jpg?raw=true)

Для удобства используйте фильтр записей по датам и времени. Например, настройте его на 
отображение утренних приемов пищи или всех приемов пищи за последнюю неделю.

![Add Meal](src/main/resources/screenshots/FilterMeal.jpg?raw=true)

Таблица с едой легко сортируется по любой колонке, есть удобный поиск.

При изменении дневного калоража Вы всегда можете изменить норму калорий в день в Вашем профиле.

![Add Meal](src/main/resources/screenshots/ProfileCalories.jpg?raw=true)

Авторизация предполагает наличие пользователей со стандартными правами и администраторов.
Администратор может управлять пользователями: активировать и деактивировать, менять учетные 
данные.

![Add Meal](src/main/resources/screenshots/Admin.jpg?raw=true)

**Стек технологий:**

Maven/ Spring/ Security/ JPA(Hibernate)/ REST(Jackson)/ Bootstrap(CSS)/ jQuery + plugins