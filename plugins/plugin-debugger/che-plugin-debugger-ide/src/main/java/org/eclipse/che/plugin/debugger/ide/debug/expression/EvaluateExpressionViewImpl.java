/*******************************************************************************
 * Copyright (c) 2012-2017 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package org.eclipse.che.plugin.debugger.ide.debug.expression;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.eclipse.che.plugin.debugger.ide.DebuggerLocalizationConstant;
import org.eclipse.che.ide.ui.window.Window;

import javax.validation.constraints.NotNull;


/**
 * The implementation of {@link EvaluateExpressionView}.
 *
 * @author <a href="mailto:aplotnikov@codenvy.com">Andrey Plotnikov</a>
 */
@Singleton
public class EvaluateExpressionViewImpl extends Window implements EvaluateExpressionView {
    interface EvaluateExpressionViewImplUiBinder extends UiBinder<Widget, EvaluateExpressionViewImpl> {
    }

    private static EvaluateExpressionViewImplUiBinder uiBinder = GWT.create(EvaluateExpressionViewImplUiBinder.class);

    @UiField
    TextBox                      expression;
    @UiField
    TextArea                     result;
    @UiField(provided = true)
    DebuggerLocalizationConstant locale;

    private Button         evaluateButton;
    private ActionDelegate delegate;

    /**
     * Create view.
     */
    @Inject
    protected EvaluateExpressionViewImpl(DebuggerLocalizationConstant locale) {
        this.locale = locale;

        Widget widget = uiBinder.createAndBindUi(this);

        this.setTitle(this.locale.evaluateExpressionViewTitle());
        this.setWidget(widget);

        Button closeButton = createButton(locale.evaluateExpressionViewCloseButtonTitle(), "debugger-close-btn", new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                delegate.onCloseClicked();
            }
        });

        evaluateButton = createButton(locale.evaluateExpressionViewEvaluateButtonTitle(), "debugger-evaluate-btn", new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                delegate.onEvaluateClicked();
            }
        });

        expression.addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                if (KeyCodes.KEY_ENTER == event.getNativeKeyCode()) {
                    delegate.onEvaluateClicked();
                }
            }
        });

        addButtonToFooter(closeButton);
        addButtonToFooter(evaluateButton);
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public String getExpression() {
        return expression.getText();
    }

    /** {@inheritDoc} */
    @Override
    public void setExpression(@NotNull String expression) {
        this.expression.setText(expression);
    }

    /** {@inheritDoc} */
    @Override
    public void setResult(@NotNull String value) {
        this.result.setText(value);
    }

    /** {@inheritDoc} */
    @Override
    public void setEnableEvaluateButton(boolean enabled) {
        evaluateButton.setEnabled(enabled);
    }

    /** {@inheritDoc} */
    @Override
    public void focusInExpressionField() {
        new Timer() {
            @Override
            public void run() {
                expression.setFocus(true);
            }
        }.schedule(300);
    }

    /** {@inheritDoc} */
    @Override
    public void close() {
        this.hide();
    }

    /** {@inheritDoc} */
    @Override
    public void showDialog() {
        this.show();
        if (!expression.getText().isEmpty()) {
            expression.selectAll();
            evaluateButton.setEnabled(true);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void setDelegate(ActionDelegate delegate) {
        this.delegate = delegate;
    }

    @UiHandler("expression")
    public void handleKeyUp(KeyUpEvent event) {
        delegate.onExpressionValueChanged();
    }
}
