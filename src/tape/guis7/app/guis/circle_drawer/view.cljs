(ns tape.guis7.app.guis.circle-drawer.view
  (:require [re-frame.core :as rf]
            [tape.mvc.view :as v :include-macros true]
            [tape.guis7.app.guis.circle-drawer.controller :as circle-drawer.c]))

;;; Helpers

(defn- create [event]
  (let [rect (.getBoundingClientRect (.-currentTarget event))
        x    (- (.-clientX event) (.-left rect))
        y    (- (.-clientY event) (.-top rect))]
    (v/dispatch [circle-drawer.c/create x y])))

(defn- select [event]
  (let [rect (.getBoundingClientRect (.-currentTarget event))
        x    (- (.-clientX event) (.-left rect))
        y    (- (.-clientY event) (.-top rect))]
    (v/dispatch [circle-drawer.c/select x y])))

;;; Views

(defn ^::v/view index []
  (let [circles    @(v/subscribe [circle-drawer.c/circles])
        selected   @(v/subscribe [circle-drawer.c/selected])
        activated? @(v/subscribe [circle-drawer.c/activated?])
        editing?   @(v/subscribe [circle-drawer.c/editing?])
        undo?      @(v/subscribe [circle-drawer.c/undo?])
        redo?      @(v/subscribe [circle-drawer.c/redo?])
        activate   (fn [event]
                     (v/dispatch [circle-drawer.c/activate])
                     (.preventDefault event))]
    [:<>
     [:div.field.is-grouped
      [:p.control
       [:button.button {:disabled (not undo?)
                        :on-click #(when undo?
                                     (v/dispatch [circle-drawer.c/undo]))} "Undo"]]
      [:p.control
       [:button.button {:disabled (not redo?)
                        :on-click #(when redo?
                                     (v/dispatch [circle-drawer.c/redo]))} "Redo"]]]

     [:div.circle-drawer {:on-click create :on-mouse-move select}
      (for [[i [x y r]] circles :let [d (* 2 r)]]
        [:div.circle {:key             i,
                      :class           (when (= i selected) "is-selected")
                      :on-context-menu (when (= i selected) activate)
                      :style           {:left (- x r) :top (- y r) :width d :height d}}])]

     [:div.modal {:class (when activated? "is-active")}
      [:div.modal-background]
      [:div.modal-content
       [:button.button.is-primary {:on-click #(v/dispatch [circle-drawer.c/edit])} "Adjust diameter..."]]
      [:button.modal-close.is-large {:on-click #(v/dispatch [circle-drawer.c/deactivate])}]]

     [:div.modal {:class (when editing? "is-active")}
      [:div.modal-background]
      [:div.modal-content
       [:input {:type      "range" :step 1 :min 0 :max 100
                :value     (-> circles (get selected) (get 2))
                :on-change #(v/dispatch [circle-drawer.c/set-radius (-> % .-target .-value js/parseInt)])}]]
      [:button.modal-close.is-large {:on-click #(v/dispatch [circle-drawer.c/stop-edit])}]]]))

;;; Module

(v/defmodule)
