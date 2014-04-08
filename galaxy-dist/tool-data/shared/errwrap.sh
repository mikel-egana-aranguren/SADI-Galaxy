#!/bin/bash

# Script for gracefully handling errors in galaxy, copied from
# https://bitbucket.org/cmungall/galaxy-obo

# Some programs send normal info to STDERR, making galaxy think an error has
# ocurred. This script, developed by Chris Mungall, fixes the problem by capturing
# the error and only sending it to STDERR if it is really an error, by lloking at
# the program's exit code

# Temporary storage for STDERR
TMP_STDERR="/tmp/errwrap-$$.tmp" || exit 1

# Run the program, send STDERR to temporary file
"$@" 2> $TMP_STDERR

#check program's exit code
if (( $? ));  then
	#Program failed, send STDERR to real STDERR
	cat $TMP_STDERR >&2
	rm $TMP_STDERR
	exit 1
fi

#Program succeeded, delete STDERR file
rm $TMP_STDERR
exit 0