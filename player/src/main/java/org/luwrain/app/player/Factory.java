// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.player;

import com.google.auto.service.*;

import org.luwrain.core.*;

import static java.util.Objects.*;

@AutoService(org.luwrain.player.Factory.class)
public class Factory implements org.luwrain.player.Factory
{
    @Override public org.luwrain.player.Player  newPlayer(Params params)
    {
	requireNonNull(params, "params can't be null");
	NullCheck.notNull(params.luwrain, "params.luwrain");
	return new Dispatcher(params.luwrain);
    }
}
