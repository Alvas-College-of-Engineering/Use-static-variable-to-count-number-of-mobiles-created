package mobilemanagement;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public final class HtmlRenderer {
    private static final NumberFormat INR = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("en-IN"));

    private HtmlRenderer() {
    }

    public static String page(List<Mobile> mobiles, int activeCount, String notice) {
        StringBuilder cards = new StringBuilder();
        for (Mobile mobile : mobiles) {
            cards.append(mobileCard(mobile));
        }

        return """
                <!doctype html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Mobile Management System</title>
                    <style>
                """ + css() + """
                    </style>
                </head>
                <body>
                    <main class="shell">
                        <section class="hero">
                            <div>
                                <p class="eyebrow">Java Dynamic Web Project</p>
                                <h1>Mobile Management System</h1>
                                <p class="subtext">Create mobile objects, store their details, and watch the static Java counter update instantly.</p>
                            </div>
                            <div class="stats" aria-label="Mobile statistics">
                                <div>
                                    <span class="stat-number">""" + Mobile.getTotalMobilesCreated() + """
                                    </span>
                                    <span class="stat-label">Total objects created</span>
                                </div>
                                <div>
                                    <span class="stat-number">""" + activeCount + """
                                    </span>
                                    <span class="stat-label">Currently stored</span>
                                </div>
                            </div>
                        </section>

                        """ + noticeBlock(notice) + """

                        <section class="workspace">
                            <form class="panel form-panel" method="post" action="/add">
                                <h2>Add Mobile</h2>
                                <label>Brand<input name="brand" placeholder="Example: Google" required></label>
                                <label>Model<input name="model" placeholder="Example: Pixel 9" required></label>
                                <label>Operating System<input name="os" placeholder="Example: Android" required></label>
                                <div class="row">
                                    <label>Storage GB<input name="storage" type="number" min="1" value="128" required></label>
                                    <label>Price INR<input name="price" type="number" min="0" step="0.01" value="29999" required></label>
                                </div>
                                <button type="submit">Create Mobile Object</button>
                            </form>

                            <section class="panel list-panel">
                                <div class="list-head">
                                    <h2>Stored Mobiles</h2>
                                    <span>""" + activeCount + """
                                     records</span>
                                </div>
                                <div class="mobile-grid">
                                    """ + cards + """
                                </div>
                            </section>
                        </section>
                    </main>
                </body>
                </html>
                """;
    }

    private static String mobileCard(Mobile mobile) {
        return """
                <article class="mobile-card">
                    <div class="card-top">
                        <span class="id">#""" + mobile.getId() + """
                        </span>
                        <span class="os">""" + escape(mobile.getOperatingSystem()) + """
                        </span>
                    </div>
                    <h3>""" + escape(mobile.getDisplayName()) + """
                    </h3>
                    <dl>
                        <div><dt>Storage</dt><dd>""" + mobile.getStorageGb() + """
                         GB</dd></div>
                        <div><dt>Price</dt><dd>""" + INR.format(mobile.getPrice()) + """
                        </dd></div>
                        <div><dt>Created</dt><dd>""" + escape(mobile.getCreatedAtDisplay()) + """
                        </dd></div>
                    </dl>
                </article>
                """;
    }

    private static String noticeBlock(String notice) {
        if (notice == null || notice.isBlank()) {
            return "";
        }
        return "<p class=\"notice\">" + escape(notice) + "</p>";
    }

    private static String escape(String value) {
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }

    private static String css() {
        return """
                * { box-sizing: border-box; }
                body {
                    margin: 0;
                    min-height: 100vh;
                    font-family: Arial, Helvetica, sans-serif;
                    color: #17202a;
                    background: #f4f7fb;
                }
                .shell {
                    width: min(1120px, calc(100% - 32px));
                    margin: 0 auto;
                    padding: 32px 0;
                }
                .hero {
                    display: grid;
                    grid-template-columns: 1.45fr .9fr;
                    gap: 24px;
                    align-items: stretch;
                    padding: 32px;
                    color: white;
                    background: linear-gradient(135deg, #12343b 0%, #256d85 48%, #29a19c 100%);
                    border-radius: 8px;
                    box-shadow: 0 20px 45px rgba(18, 52, 59, .2);
                }
                .eyebrow {
                    margin: 0 0 10px;
                    text-transform: uppercase;
                    font-size: 12px;
                    font-weight: 700;
                    letter-spacing: 2px;
                    color: #d8f3dc;
                }
                h1, h2, h3, p { margin-top: 0; }
                h1 {
                    margin-bottom: 12px;
                    font-size: clamp(32px, 6vw, 58px);
                    line-height: 1;
                    letter-spacing: 0;
                }
                .subtext {
                    max-width: 620px;
                    margin-bottom: 0;
                    color: #edf8f7;
                    font-size: 17px;
                    line-height: 1.6;
                }
                .stats {
                    display: grid;
                    grid-template-columns: 1fr 1fr;
                    gap: 14px;
                    align-content: end;
                }
                .stats div, .panel, .notice {
                    background: rgba(255, 255, 255, .94);
                    border: 1px solid rgba(23, 32, 42, .08);
                    border-radius: 8px;
                }
                .stats div {
                    min-height: 142px;
                    padding: 22px;
                    color: #12343b;
                    display: flex;
                    flex-direction: column;
                    justify-content: center;
                }
                .stat-number {
                    display: block;
                    font-size: 48px;
                    font-weight: 800;
                }
                .stat-label {
                    color: #46636b;
                    font-size: 14px;
                    line-height: 1.35;
                }
                .notice {
                    margin: 20px 0 0;
                    padding: 14px 16px;
                    color: #14532d;
                    border-color: #bbf7d0;
                    background: #f0fdf4;
                    font-weight: 700;
                }
                .workspace {
                    display: grid;
                    grid-template-columns: 360px 1fr;
                    gap: 24px;
                    margin-top: 24px;
                    align-items: start;
                }
                .panel {
                    padding: 22px;
                    box-shadow: 0 16px 36px rgba(32, 44, 57, .08);
                }
                .panel h2 {
                    margin-bottom: 18px;
                    font-size: 22px;
                }
                label {
                    display: block;
                    margin-bottom: 14px;
                    color: #334155;
                    font-size: 13px;
                    font-weight: 700;
                }
                input {
                    display: block;
                    width: 100%;
                    height: 44px;
                    margin-top: 7px;
                    padding: 0 12px;
                    border: 1px solid #cbd5e1;
                    border-radius: 6px;
                    color: #17202a;
                    background: #ffffff;
                    font: inherit;
                }
                input:focus {
                    outline: 3px solid rgba(41, 161, 156, .22);
                    border-color: #29a19c;
                }
                .row {
                    display: grid;
                    grid-template-columns: 1fr 1fr;
                    gap: 12px;
                }
                button {
                    width: 100%;
                    height: 46px;
                    border: 0;
                    border-radius: 6px;
                    color: white;
                    background: #d9480f;
                    font-size: 15px;
                    font-weight: 800;
                    cursor: pointer;
                }
                button:hover { background: #b93807; }
                .list-head {
                    display: flex;
                    justify-content: space-between;
                    align-items: center;
                    gap: 12px;
                    margin-bottom: 18px;
                }
                .list-head h2 { margin-bottom: 0; }
                .list-head span {
                    color: #64748b;
                    font-size: 13px;
                    font-weight: 700;
                }
                .mobile-grid {
                    display: grid;
                    grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
                    gap: 14px;
                }
                .mobile-card {
                    min-height: 210px;
                    padding: 18px;
                    border: 1px solid #e2e8f0;
                    border-radius: 8px;
                    background: #ffffff;
                }
                .card-top {
                    display: flex;
                    justify-content: space-between;
                    align-items: center;
                    gap: 10px;
                    margin-bottom: 14px;
                }
                .id, .os {
                    padding: 6px 9px;
                    border-radius: 999px;
                    font-size: 12px;
                    font-weight: 800;
                }
                .id { color: #17494d; background: #dff7f5; }
                .os { color: #7c2d12; background: #ffedd5; }
                .mobile-card h3 {
                    min-height: 52px;
                    margin-bottom: 16px;
                    font-size: 21px;
                    line-height: 1.25;
                }
                dl { margin: 0; }
                dl div {
                    display: flex;
                    justify-content: space-between;
                    gap: 12px;
                    padding: 9px 0;
                    border-top: 1px solid #eef2f7;
                }
                dt { color: #64748b; }
                dd {
                    margin: 0;
                    text-align: right;
                    font-weight: 800;
                }
                @media (max-width: 820px) {
                    .hero, .workspace { grid-template-columns: 1fr; }
                    .stats, .row { grid-template-columns: 1fr; }
                    .shell { width: min(100% - 20px, 1120px); padding: 16px 0; }
                    .hero, .panel { padding: 18px; }
                }
                """;
    }
}
