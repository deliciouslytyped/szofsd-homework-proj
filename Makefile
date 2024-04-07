# TODO I should probably do something like xvfb-run and auto-find a free display?
# This DISPLAY is specific to my dev environment, this is what SSH gives me by default.
# I don't know if it's a common value.
export DISPLAY=localhost:10.0
GUESTDISPLAY ?= :2
SCREEN ?= 1440x900x24
MVN ?= ./mvnw
WM ?= awesome
SCENEBUILDER ?= /opt/scenebuilder/bin/SceneBuilder
# Pass -Djava17
JAVA17 ?=

watch:
	{ \
	[ -z "$$DEBUG" ] || set -x ;\
	[ -z "$$DISPLAY" ] && { echo '$$DISPLAY not set or empty.' ; exit 1; } ;\
	Xephyr $(GUESTDISPLAY) -screen $(SCREEN) & \
	{ sleep 2; DISPLAY=$(GUESTDISPLAY) $(WM); } & \
	`#TODO I don't think this kills the entire process tree, I think processes are being left around?`; \
	while true; do inotifywait -e modify,delete,create,move -r src; kill -9 "$$thejob"; DISPLAY=$(GUESTDISPLAY) $(MVN) $(JAVA17) clean package javafx:run & thejob="$$!"; done; \
	}

scenebuilder:
	( Xephyr $(GUESTDISPLAY) -screen $(SCREEN) & { sleep 2; DISPLAY=$(GUESTDISPLAY) $(WM); } & DISPLAY=$(GUESTDISPLAY) $(SCENEBUILDER) )
