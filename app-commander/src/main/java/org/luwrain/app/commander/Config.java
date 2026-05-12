// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.commander;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
class Config
{
    private boolean showHidden;
    private String zipFilesEncoding;
    }
