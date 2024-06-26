package com.example.postgres.view.list;

import com.example.postgres.classes.Channel;
import com.example.postgres.dto.UserDetailsDto;
import com.example.postgres.service.frontend.ChannelServiceFrontend;
import com.example.postgres.utils.UserUtils;
import com.example.postgres.view.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.jetbrains.annotations.NotNull;

@PageTitle("My Profile")
@Route(value="profile", layout = MainLayout.class)
public class ProfileView extends VerticalLayout {
    private final static Binder<UserDetailsDto> binder = new Binder<>(UserDetailsDto.class);

    private final Span statusLabel;

    private final ChannelServiceFrontend channelServiceFrontend;

    public ProfileView(ChannelServiceFrontend channelServiceFrontend) {
        this.channelServiceFrontend = channelServiceFrontend;

        statusLabel = new Span("Loading ...");
        statusLabel.setVisible(false);

        getProfile();

        add(statusLabel);

    }



    private void getProfile()
    {
        statusLabel.setVisible(true);
        UserDetailsDto user = new UserDetailsDto();
        UserUtils.showStoredValue(user, UI.getCurrent(), () -> {

            if (user.getAccessToken() == null) {
                statusLabel.setText("Please log in.");
                statusLabel.addClassNames(LumoUtility.TextColor.ERROR);
                statusLabel.setVisible(true);
                Notification notification = Notification
                        .show("Please log in.", 3000, Notification.Position.TOP_CENTER);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }


            statusLabel.setVisible(false);

            HorizontalLayout mainLayout = new HorizontalLayout();

            VerticalLayout card = new VerticalLayout();
            card.getStyle().set("border", "1px solid #000000");
            card.getStyle().set("border-radius", "10px");
            card.getStyle().set("padding", "1em");
            card.setWidth("300px");


            H2 header = new H2("Profile Details");
            header.addClassNames(LumoUtility.Margin.Top.NONE, LumoUtility.Margin.Bottom.MEDIUM);
            header.getStyle().set("text-align", "center");

            H5 nameheader = new H5("Name");
            nameheader.addClassNames(LumoUtility.Margin.Top.NONE, LumoUtility.Margin.Bottom.NONE);
            Div name = new Div();
            TextField nameField = new TextField();
            nameField.setReadOnly(true);
            binder.bind(nameField, UserDetailsDto::getName, UserDetailsDto::setName);
            nameField.getStyle().set("font-size", "1em");
            nameField.getStyle().set("font-weight", "bold");
            nameField.getStyle().set("margin-bottom", "0.5em");
            nameField.getStyle().set("text-align", "center");
            name.add( nameField);


            H5 emailheader = new H5("Email");
            emailheader.addClassNames(LumoUtility.Margin.Top.NONE, LumoUtility.Margin.Bottom.NONE);
            Div email = new Div();
            TextField emailField = new TextField();
            emailField.setReadOnly(true);
            binder.bind(emailField, UserDetailsDto::getUser_email, UserDetailsDto::setUser_email);
            emailField.getStyle().set("font-size", "1em");
            emailField.getStyle().set("font-weight", "bold");
            emailField.getStyle().set("margin-bottom", "0.5em");
            emailField.getStyle().set("text-align", "center");
            email.add( emailField);

            H5 usernameheader = new H5("Username");
            usernameheader.addClassNames(LumoUtility.Margin.Top.NONE, LumoUtility.Margin.Bottom.NONE);
            Div username = new Div();
            TextField usernameField = new TextField();
            usernameField.setReadOnly(true);
            binder.bind(usernameField, UserDetailsDto::getUser_name, UserDetailsDto::setUser_name);
            usernameField.getStyle().set("font-size", "1em");
            usernameField.getStyle().set("font-weight", "bold");
            usernameField.getStyle().set("margin-bottom", "0.5em");
            usernameField.getStyle().set("text-align", "center");
            username.add( usernameField);

            card.add(header, nameheader, name, emailheader, email, usernameheader, username);
            mainLayout.add(card);

            VerticalLayout channelLayout = new VerticalLayout();
            channelLayout.setWidth("500px"); // Set the width of the channel view
            channelLayout.getStyle().set("border", "1px solid #000000"); // Set border color to dark blue
            channelLayout.getStyle().set("border-radius", "10px"); // Set border radius
            channelLayout.getStyle().set("padding", "1em"); // Set padding


            Div channelHeader = new Div();
            channelHeader.setText("My Channels");
            channelHeader.getStyle().set("font-size", "1.5em");
            channelHeader.getStyle().set("font-weight", "bold");
            channelHeader.getStyle().set("margin-bottom", "0.5em");
            channelHeader.getStyle().set("text-align", "center");
            channelLayout.add(channelHeader);


            VirtualList<Channel> virtualList = getChannelVirtualList();

            channelLayout.add(virtualList);
            mainLayout.add(channelLayout); // Add the channelLayout to the mainLayout
            add(mainLayout); // Add the mainLayout to the ProfileView



            binder.setBean(user);
            channelServiceFrontend.setChannels(virtualList, user.getAccessToken(),user.getUser_id());
            setSizeFull();
            setJustifyContentMode(JustifyContentMode.CENTER);
            setDefaultHorizontalComponentAlignment(Alignment.CENTER);
            UI.getCurrent().getElement().getThemeList().add("dark");


            });
    }

    @NotNull
    private static VirtualList<Channel> getChannelVirtualList() {
        VirtualList<Channel> virtualList = new VirtualList<>();

        virtualList.setRenderer(new ComponentRenderer<>(channel -> {
            HorizontalLayout layout = new HorizontalLayout();
            layout.setAlignItems(Alignment.CENTER);
            layout.setSpacing(true);
            layout.setPadding(true);

            Div channelLink = new Div();
            channelLink.setText(channel.getName());
            channelLink.getStyle().set("font-weight", "bold");
            channelLink.addClickListener(e -> {
                System.out.println(channel.getId());
                System.out.println(channel.getName());
                UI.getCurrent().navigate(ChannelView.class, new RouteParameters("channelName",channel.getName()));
            });


            Div description = new Div();
            description.setText(channel.getDescription());



            layout.add(channelLink, description);
            return layout;
        }));
        return virtualList;
    }
}
