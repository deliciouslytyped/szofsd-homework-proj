GUESTDISPLAY ?= :2
SCREEN ?= 1440x900x24
MVN ?= ./mvnw
WM ?= awesome

watch:
	{ \
	[ -z "$$DEBUG" ] || set -x ;\
	[ -z "$$DISPLAY" ] && { echo '$$DISPLAY not set or empty.' ; exit 1; } ;\
	Xephyr $(GUESTDISPLAY) -screen $(SCREEN) & \
	{ sleep 2; DISPLAY=$(GUESTDISPLAY) $(WM); } & \
	`#TODO I don't think this kills the entire process tree, I think processes are being left around?`; \
	while true; do inotifywait -e modify,delete,create,move -r src; kill -9 "$$thejob"; DISPLAY=$(GUESTDISPLAY) $(MVN) clean package javafx:run & thejob="$$!"; done; \
	}
