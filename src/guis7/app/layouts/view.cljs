(ns guis7.app.layouts.view
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [tape.mvc.view :as v]
            [tape.router :as router]
            [tape.toasts.view :as toasts.v]
            [guis7.app.home.controller :as home.c]
            [guis7.app.guis.counter.controller :as counter.c]
            [guis7.app.guis.temperature-converter.controller :as temperature-converter.c]
            [guis7.app.guis.flight-booker.controller :as flight-booker.c]
            [guis7.app.guis.timer.controller :as timer.c]
            [guis7.app.guis.crud.controller :as crud.c]
            [guis7.app.guis.circle-drawer.controller :as circle-drawer.c]
            [guis7.app.guis.cells.controller :as cells.c]))

(defn- navbar-item [current-view route name]
  (let [maybe-active (when (= current-view route) "is-active")]
    [:a.navbar-item.is-tab
     {:href (router/href [route]) :class maybe-active}
     name]))

(defn- navbar []
  (let [burger-active (r/atom false)]
    (fn []
      (let [burger-class  (when @burger-active "is-active")
            burger-toggle #(swap! burger-active not)
            current-view  @(rf/subscribe [::v/current])
            navbar-item   (partial navbar-item current-view)]
        [:nav.navbar
         [:div.container
          [:div.navbar-brand
           (navbar-item ::home.c/index [:span.has-text-link.is-size-4.has-text-weight-bold "7GUIs"])
           [:a.navbar-burger.burger {:class burger-class :on-click burger-toggle}
            [:span] [:span] [:span]]]
          [:div.navbar-menu {:class burger-class}
           [:div.navbar-start
            (navbar-item ::counter.c/index "Counter")
            (navbar-item ::temperature-converter.c/index "Temperature converter")
            (navbar-item ::flight-booker.c/index "Flight booker")
            (navbar-item ::timer.c/index "Timer")
            (navbar-item ::crud.c/index "CRUD")
            (navbar-item ::circle-drawer.c/index "Circle Drawer")
            (navbar-item ::cells.c/index "Cells")]]]]))))

(defn app []
  (let [current-view-fn @(rf/subscribe [::v/current-fn])]
    [:<>
     [navbar]
     [:section.section
      [:div.container
       (when (some? current-view-fn)
         [current-view-fn])]]
     [:footer.footer
      [:div.container
       "© 2020 clyfe"]]
     [toasts.v/index]]))
