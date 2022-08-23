TEMPLATE = app
CONFIG += console c++11
CONFIG -= app_bundle
CONFIG -= qt

SOURCES += \
        ai.cpp \
        card.cpp \
        main.cpp \
        move.cpp \
        player.cpp \
        test.cpp \
        tile.cpp

DISTFILES += \
    Player.pro.user

HEADERS += \
    ai.h \
    move.h \
    player.h \
    test.h \
    tile.h
