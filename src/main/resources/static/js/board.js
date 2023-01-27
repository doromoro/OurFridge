'use strict';

let index = {
    init: function () {
        $("#btn-save").on("click", () => {
            this.save();
        });
    //     $("#btn-delete").on("click", () => {
    //         this.deleteById();
    //     });
    //     $("#btn-update").on("click", () => {
    //         this.update();
    //     });
    },

    save: function () {
        let data = {
            title: $("#title").val(),
            contents: $("#contents").val()
        }

        $.ajax({
            type: "POST",
            url: "/api/recipe",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json"
        }).done(function (res) {
            alert("글작성이 완료되었습니다.");
            location.href = "/recipe";
        }).fail(function (err) {
            alert(JSON.stringify(err));
        });
    },

    // deleteById: function () {
    //     let id = $("#id").text();
    //
    //     $.ajax({
    //         type: "DELETE",
    //         url: "/api/v1/recipe/" + id,
    //         dataType: "json"
    //     }).done(function (res) {
    //         alert("글삭제가 완료되었습니다.");
    //         location.href = "/";
    //     }).fail(function (err) {
    //         alert(JSON.stringify(err));
    //     });
    // },

    // update: function () {
    //     let id = $("#id").val();
    //
    //     let data = {
    //         title: $("#title").val(),
    //         content: $("#content").val()
    //     }
    //     console.log(id);
    //     console.log(data);
    //
    //     $.ajax({
    //         type: "PUT",
    //         url: "/api/recipe/",
    //         data: JSON.stringify(data),
    //         contentType: "application/json; charset=utf-8",
    //         dataType: "json"
    //     }).done(function (res) {
    //         alert("글수정이 완료되었습니다.");
    //         location.href = "/recipe";
    //     }).fail(function (err) {
    //         alert(JSON.stringify(err));
    //     });
    // }
}
index.init();