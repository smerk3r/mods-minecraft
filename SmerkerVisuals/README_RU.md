# SmerkerVisuals

Собственный клиентский визуальный Fabric-мод для Minecraft 1.21.11.

## Функции версии 1.0.0

- Меню визуальных модулей по **Right Shift**.
- FPS HUD — FPS в левом верхнем углу.
- Coordinates HUD — координаты игрока.
- Keystrokes — отображение WASD и прыжка.
- Minimal particles — переключение частиц на минимальный уровень.
- Сохранение включённых модулей в `.minecraft/config/smerkervisuals.properties`.
- Автоматическая сборка JAR через GitHub Actions.

Это самостоятельная реализация визуальных HUD-модулей со своим названием и оформлением, а не копирование исходного кода или фирменной графики FlyVisuals. В этой версии нет ESP сквозь стены, автоприцеливания или автоматизации боя.

## Установка

1. Установи Fabric Loader для Minecraft 1.21.11.
2. Установи Fabric API для Minecraft 1.21.11.
3. Скачай JAR из Artifacts последней успешной сборки GitHub Actions.
4. Положи JAR в `.minecraft/mods`.
5. Запусти игру через профиль Fabric и нажми **Right Shift**.

## Сборка

Нужен JDK 25 для Gradle/Loom; мод компилируется с target Java 21.

```bash
gradle --no-daemon build
```

Установочный JAR будет в `build/libs/` (файл без `-sources`).
